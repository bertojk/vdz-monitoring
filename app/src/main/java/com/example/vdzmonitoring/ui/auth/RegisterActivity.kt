package com.example.vdzmonitoring.ui.auth

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.vdzmonitoring.R
import com.example.vdzmonitoring.databinding.ActivityRegisterBinding
import com.example.vdzmonitoring.ui.home.HomeActivity
import com.example.vdzmonitoring.util.*
import kotlinx.android.synthetic.main.activity_register.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class RegisterActivity :
    AppCompatActivity(),
    AuthListener,
    KodeinAware {

    override val kodein by kodein()

    private val factory: AuthViewModelFactory by instance()

    private lateinit var imei: String

    @SuppressLint( "NewApi", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityRegisterBinding>(this, R.layout.activity_register)

        try {
            imei = (this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).imei ?: ""

            val viewModel = ViewModelProviders.of(this, factory)
                .get(AuthViewModel::class.java)
            binding.viewModel = viewModel
            viewModel.authListener = this
            binding.registerButton.setOnClickListener {
                viewModel.register(imei)
            }


        } catch (e: Exception) {

        }
    }

    override fun onStarted() {
        register_layout.hide()
        progressBar.show()
    }

    override fun onSuccess() {
        register_layout.show()
        progressBar.hide()
        changeActivity(HomeActivity::class.java)
    }

    override fun onFailure(message: String) {
        register_layout.show()
        progressBar.hide()
        register_layout.snackbar(message)
    }
}
