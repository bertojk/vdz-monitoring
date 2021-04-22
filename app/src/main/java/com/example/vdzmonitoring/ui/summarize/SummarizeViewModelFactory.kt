package com.example.vdzmonitoring.ui.summarize

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vdzmonitoring.data.repositories.RouteRepository
import com.example.vdzmonitoring.data.repositories.SummaryRepository
import com.example.vdzmonitoring.data.repositories.UserRepository

@Suppress("UNCHECKED_CAST")
class SummarizeViewModelFactory(
    private val repo: SummaryRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SummarizeViewModel(repo) as T
    }
}