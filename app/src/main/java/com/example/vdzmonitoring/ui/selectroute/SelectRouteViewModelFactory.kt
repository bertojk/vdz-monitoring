package com.example.vdzmonitoring.ui.selectroute

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vdzmonitoring.data.repositories.RouteRepository
import com.example.vdzmonitoring.data.repositories.UserRepository

@Suppress("UNCHECKED_CAST")
class SelectRouteViewModelFactory(
    private val repo: RouteRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SelectRouteViewModel(repo) as T
    }
}