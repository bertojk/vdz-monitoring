package com.example.vdzmonitoring.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vdzmonitoring.data.repositories.HistoryRepository
import com.example.vdzmonitoring.data.repositories.UserRepository
import com.example.vdzmonitoring.ui.auth.LoginActivity
import com.example.vdzmonitoring.util.changeActivity
import com.example.vdzmonitoring.util.lazyDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeViewModel(
    private val userRepo: UserRepository,
    private val historyRepo: HistoryRepository
) : ViewModel() {

    var userDetail = userRepo.currentUser()

    val histories by lazyDeferred {  historyRepo.getHistories() }


    fun logout(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userRepo.logout()
            }
            context.changeActivity(LoginActivity::class.java)
        }
    }


}