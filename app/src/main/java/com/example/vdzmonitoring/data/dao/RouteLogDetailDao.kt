package com.example.vdzmonitoring.data.dao

import androidx.room.*
import com.example.vdzmonitoring.data.entities.RouteLogDetail

@Dao
interface RouteLogDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRouteLogDetail(routeLogDetail: RouteLogDetail) : Long

    @Transaction
    suspend fun insertAllRouteLogDetails(routeLogDetails: List<RouteLogDetail>) {
        routeLogDetails.subList(1, routeLogDetails.size).forEach {
            insertRouteLogDetail(it)
        }
    }

    @Query("SELECT * FROM routelogdetail where routeLogId = :routeLogId")
    fun getDetails(routeLogId: Long) : List<RouteLogDetail>

    @Query("DELETE FROM routelogdetail")
    fun deleteAllRouteLogDetails()
}