package com.example.vdzmonitoring.data.repositories

import com.example.vdzmonitoring.data.database.AppDatabase

class HistoryRepository(
    private val db: AppDatabase
) {

    fun getHistories() =
        db.getHistoryDao().getHistories()
}