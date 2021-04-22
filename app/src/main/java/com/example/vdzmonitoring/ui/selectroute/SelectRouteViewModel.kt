package com.example.vdzmonitoring.ui.selectroute

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vdzmonitoring.data.repositories.RouteRepository
import com.example.vdzmonitoring.util.lazyDeferred

class SelectRouteViewModel(
    private val repo: RouteRepository
) : ViewModel() {

    val routes by lazyDeferred{
       repo.fetchRoute()
    }

    private val _selectedRouteIndex = MutableLiveData<Long>()
    val selectedRouteIndex: LiveData<Long>
        get() = _selectedRouteIndex

    init {
        _selectedRouteIndex.postValue(-1)
    }

    fun changeIndex(idx: Long) {
        _selectedRouteIndex.postValue(idx)
    }
}
