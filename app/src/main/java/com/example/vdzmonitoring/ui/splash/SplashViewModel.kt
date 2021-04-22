package com.example.vdzmonitoring.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*

class SplashViewModel : ViewModel() {

    private val _isUserNull = MutableLiveData<Boolean>()
    val isUserNull: LiveData<Boolean>
        get() = _isUserNull

    fun changeScreen() {
        val uid = FirebaseAuth.getInstance().uid
        _isUserNull.postValue(uid == null)
    }
}