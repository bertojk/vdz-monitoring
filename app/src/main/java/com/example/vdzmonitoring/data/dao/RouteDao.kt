package com.example.vdzmonitoring.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vdzmonitoring.data.entities.Route

@Dao
interface RouteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutes(routes: List<Route>)

    @Query("SELECT * FROM route")
    fun getRoutes() : LiveData<List<Route>>

    @Query("DELETE FROM route")
    fun deleteAllRoutes()

    @Query("SELECT * FROM route WHERE routeId = :routeId")
    fun getRoute(routeId: Long): Route
}