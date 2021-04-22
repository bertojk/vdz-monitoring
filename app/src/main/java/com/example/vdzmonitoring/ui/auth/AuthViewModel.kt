package com.example.vdzmonitoring.ui.auth

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.vdzmonitoring.data.entities.User
import com.example.vdzmonitoring.data.repositories.RouteRepository
import com.example.vdzmonitoring.data.repositories.UserRepository
import com.example.vdzmonitoring.util.changeActivity
import com.example.vdzmonitoring.util.now
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AuthViewModel(
    private val repo: UserRepository,
    private val routeRepo: RouteRepository
) : ViewModel() {

    var firstName: String? = null
    var lastName: String? = null
    var phoneNo: String? = null
    var email: String? = null
    var password: String? = null

    var authListener: AuthListener? = null

    fun login(imei: String) {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Invalid email or password!!")
            return
        }

        authListener?.onStarted()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                repo.login(email!!, password!!)?.let {
                    if(it.phoneIMEI == imei) {
                        repo.saveUser(it)
                        withContext(Dispatchers.IO) {
                            routeRepo.getRouteLogs(it.uid)
                            routeRepo.getRouteLogDetails(it.uid)
                            routeRepo.fetchRoute().value
                        }
                        authListener?.onSuccess()
                    } else {
                        authListener?.onFailure("LOGIN FAIL!! Your IMEI Phone is not registered!!")
                    }
                }
            } catch (e: FirebaseAuthException) {
                e.message?.let { authListener?.onFailure(it) }
            }
        }
    }

    fun toRegister(v: View) {
        v.context.changeActivity(RegisterActivity::class.java)
    }

    fun toLogIn(v: View) {
        v.context.changeActivity(LoginActivity::class.java)
    }

    fun register(imei: String) {
        authListener?.onStarted()

        if (isAnyFieldEmpty()) {
            authListener?.onFailure("Please input all values!!")
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val u = generateUser(imei)
                repo.register(u, password!!).let {
                    authListener?.onSuccess()
                    repo.saveUser(it)
                }
            } catch (e: FirebaseAuthException) {
                e.message?.let { authListener?.onFailure(it) }
            }
        }
    }

    private fun isAnyFieldEmpty() =
        firstName.isNullOrEmpty() or
                lastName.isNullOrEmpty() or
                phoneNo.isNullOrEmpty() or
                email.isNullOrEmpty() or
                password.isNullOrEmpty()


    @SuppressLint("MissingPermission")
    private fun generateUser(imei: String) = User(
        firstName = firstName!!,
        lastName = lastName!!,
        phoneNo = phoneNo!!,
        email = email!!,
        createdAt = now(),
        updatedAt = "",
        phoneMerk = Build.BRAND,
        phoneType = Build.MODEL,
        phoneIMEI = imei
    )
}