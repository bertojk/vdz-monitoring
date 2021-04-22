package com.example.vdzmonitoring.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vdzmonitoring.data.repositories.RouteRepository
import com.example.vdzmonitoring.data.repositories.UserRepository

@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory(
    private val userRepo: UserRepository,
    private val routeRepo: RouteRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewModel(userRepo, routeRepo) as T
    }
}