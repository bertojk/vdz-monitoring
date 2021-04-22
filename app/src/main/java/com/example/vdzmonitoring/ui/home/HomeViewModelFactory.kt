package com.example.vdzmonitoring.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vdzmonitoring.data.repositories.HistoryRepository
import com.example.vdzmonitoring.data.repositories.UserRepository

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(
    private val userRepo: UserRepository,
    private val historyRepo: HistoryRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(userRepo, historyRepo) as T
    }
}