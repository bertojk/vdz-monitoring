package com.example.vdzmonitoring.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vdzmonitoring.data.repositories.UserRepository

@Suppress("UNCHECKED_CAST")
class SplashViewModelFactory(
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashViewModel() as T
    }
}