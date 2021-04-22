package com.example.vdzmonitoring.ui.summarize

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vdzmonitoring.data.entities.Route
import com.example.vdzmonitoring.data.entities.RouteLog
import com.example.vdzmonitoring.data.repositories.SummaryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SummarizeViewModel(
    private val repo: SummaryRepository
) : ViewModel() {

    private val _summarizeList = MutableLiveData<List<Summary>>()
    val summarizeList: LiveData<List<Summary>>
        get() = _summarizeList

    fun summarizeListInit(routeLogId: Long) {
        viewModelScope.launch {
            val summaries = viewModelScope.async {
                listOf(
                    repo.getRoute(routeLogId),
                    repo.getRouteDistance(routeLogId),
                    repo.getAverageSpeed(routeLogId),
                    repo.getMaxSpeed(routeLogId),
                    repo.getDuration(routeLogId),
                    repo.getStartDateTime(routeLogId),
                    repo.getEndDateTime(routeLogId)
                )
            }
            _summarizeList.postValue(summaries.await())
        }
    }

}