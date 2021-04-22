package com.example.vdzmonitoring.ui.auth

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.vdzmonitoring.R
import com.example.vdzmonitoring.databinding.ActivityLoginBinding
import com.example.vdzmonitoring.ui.home.HomeActivity
import com.example.vdzmonitoring.util.changeActivity
import com.example.vdzmonitoring.util.hide
import com.example.vdzmonitoring.util.show
import com.example.vdzmonitoring.util.snackbar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.progressBar
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.lang.Exception

class LoginActivity :
    AppCompatActivity(),
    AuthListener,
    KodeinAware {

    override val kodein by kodein()

    private val factory: AuthViewModelFactory by instance()

    private lateinit var viewModel: AuthViewModel

    private lateinit var imei: String

    @SuppressLint("MissingPermission", "NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)

        try {
            imei = (this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).imei ?: ""

            viewModel = ViewModelProviders.of(this, factory)
                .get(AuthViewModel::class.java)
            binding.viewModel = viewModel
            viewModel.authListener = this
            login_button.setOnClickListener {
                viewModel.login(imei)
            }


        } catch (e: Exception) {

        }


    }

    override fun onStarted() {
        login_layout.hide()
        progressBar.show()
    }

    override fun onSuccess() {
        login_layout.show()
        progressBar.hide()
        changeActivity(HomeActivity::class.java)
    }

    override fun onFailure(message: String) {
        login_layout.show()
        progressBar.hide()
        login_layout.snackbar(message)
    }
}
