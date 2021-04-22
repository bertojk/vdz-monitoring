package com.example.vdzmonitoring.ui.routing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vdzmonitoring.data.repositories.RouteRepository
import com.example.vdzmonitoring.data.repositories.UserRepository
import com.example.vdzmonitoring.util.TAG_ROUTE_ID_SELECTED

class RoutingViewModelFactory(
    private val userRepo: UserRepository,
    private val routeRepo: RouteRepository,
    private val activity: RoutingActivity
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoutingViewModel::class.java)) {
            return RoutingViewModel(userRepo, routeRepo, activity.intent.extras!!.getLong(
                TAG_ROUTE_ID_SELECTED
            )) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}