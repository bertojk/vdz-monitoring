package com.example.vdzmonitoring.ui.splash

import android.Manifest
import android.Manifest.permission.READ_PHONE_STATE
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.vdzmonitoring.R
import com.example.vdzmonitoring.ui.auth.RegisterActivity
import com.example.vdzmonitoring.ui.home.HomeActivity
import com.example.vdzmonitoring.util.LOCATION_PERMISSION_CODE
import com.example.vdzmonitoring.util.READ_PHONE_STATE_PERMISSION_CODE
import com.example.vdzmonitoring.util.WRITE_STORAGE_PERMISSION_CODE
import com.example.vdzmonitoring.util.changeActivity
import kotlin.system.exitProcess

class SplashActivity :
    AppCompatActivity() {



    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)


        viewModel.isUserNull.observe(this, Observer {
                if (it) {
                    changeActivity(RegisterActivity::class.java)
                } else {
                    changeActivity(HomeActivity::class.java)
                }

        })
        verifyPermission()
    }

    private fun verifyPermission() {
        val permissionReadPhoneState = ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE)
        val permissionAccessFineLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val permissionWriteExternalStorage = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if(permissionReadPhoneState != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(READ_PHONE_STATE), READ_PHONE_STATE_PERMISSION_CODE)
        if(permissionAccessFineLocation != PackageManager.PERMISSION_GRANTED )
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_CODE)
        if(permissionWriteExternalStorage != PackageManager.PERMISSION_GRANTED )
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_STORAGE_PERMISSION_CODE)

        if(arrayOf(permissionAccessFineLocation, permissionReadPhoneState, permissionWriteExternalStorage).all {
                it == PackageManager.PERMISSION_GRANTED
            }) viewModel.changeScreen()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(grantResults.isNotEmpty() &&
            grantResults.all { it == PackageManager.PERMISSION_GRANTED } )
            viewModel.changeScreen()
        else {
            exitProcess(0)
        }
        return
    }
}
