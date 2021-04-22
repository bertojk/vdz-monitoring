package com.example.vdzmonitoring.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.vdzmonitoring.data.entities.Route
import com.example.vdzmonitoring.data.entities.RouteLog
import com.example.vdzmonitoring.data.entities.RouteLogDetail
import com.example.vdzmonitoring.data.entities.User

@Dao
interface RouteLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRouteLog(routeLog: RouteLog): Long

    @Transaction
    suspend fun insertAllRouteLogs(routeLogs: List<RouteLog>) {
        routeLogs.subList(1, routeLogs.size).forEach {
            insertRouteLog(it)
        }
    }

    @Query("SELECT * FROM routelog where routeLogId = :routeLogId")
    fun getLog(routeLogId: Long) : RouteLog

    @Query("DELETE FROM routelog")
    fun deleteAllRouteLogs()

    @Query("SELECT * FROM routelog")
    fun getAllLogs() : LiveData<List<RouteLog>>
}