package com.example.vdzmonitoring.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.vdzmonitoring.ui.home.History

@Dao
interface HistoryDao {

    @Query("SELECT * FROM RouteLog log JOIN ROUTE r ON log.routeId = r.routeId ORDER BY log.routeLogId DESC")
    fun getHistories(): LiveData<List<History>>
}