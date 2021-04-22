package com.example.vdzmonitoring.data.repositories

import com.example.vdzmonitoring.data.database.AppDatabase
import com.example.vdzmonitoring.ui.summarize.Summary
import com.example.vdzmonitoring.util.round
import com.example.vdzmonitoring.util.toDateTime
import com.example.vdzmonitoring.util.toTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.osmdroid.util.GeoPoint
import java.sql.Timestamp


private const val ROUTE_NAME_LABEL = "Route"
private const val DISTANCE_LABEL = "Distance in km"
private const val MAX_SPEED_LABEL = "Max Speed in km/h"
private const val AVG_SPEED_LABEL = "Average Speed in km/h"
private const val DURATION_LABEL = "Duration"
private const val START_DATETIME_LABEL = "Start at"
private const val END_DATETIME_LABEL = "End at"

class SummaryRepository(
    private val db: AppDatabase
) {

    suspend fun getRoute(routeLogId: Long): Summary {
        val r = CoroutineScope(Dispatchers.IO).async {
            db.getRouteLogDao().getLog(routeLogId).routeId.let {
                getRouteByRouteLogId(it)
            }.routeName
        }
        return Summary(ROUTE_NAME_LABEL, r.await())
    }

    suspend fun getRouteDistance(routeLogId: Long): Summary {
        val r = CoroutineScope(Dispatchers.IO).async {
            getRouteLogDetails(routeLogId)
        }

        val details = r.await()

        val initGeoPoint = details.firstOrNull()?.let {
            GeoPoint(it.latitude, it.longitude)
        }

        val route = r.await().map {
            GeoPoint(it.latitude, it.longitude)
        }

        var totalDist = 0.0
        var before = initGeoPoint

        route.forEachIndexed { index, geoPoint ->
            if(index != 0)
            {
                totalDist += geoPoint.distanceToAsDouble(before)
                before = geoPoint
            }
        }
        return Summary(DISTANCE_LABEL, totalDist.div(1000).round())
    }

    suspend fun getMaxSpeed(routeLogId: Long): Summary {
        val r = CoroutineScope(Dispatchers.IO).async {
            getRouteLogDetails(routeLogId).maxBy {
                it.speed
            }
        }

        return Summary (MAX_SPEED_LABEL, r.await()!!.speed.toString())
    }

    suspend fun getAverageSpeed(routeLogId: Long): Summary {
        val r = CoroutineScope(Dispatchers.IO).async {
            getRouteLogDetails(routeLogId).map {
                it.speed
            }.reduce { sum, speed ->
                sum + speed
            }.div(getRouteLogDetails(routeLogId).size)
        }
        return Summary(AVG_SPEED_LABEL, r.await().toString())
    }

    suspend fun getDuration(routeLogId: Long) =
        Summary(
            DURATION_LABEL,
            Timestamp(
                endDateTimeAsync(routeLogId).await()
                        - startDateTimeAsync(routeLogId).await())
                .time.toTime()
        )


    suspend fun getStartDateTime(routeLogId: Long) =
        Summary(START_DATETIME_LABEL, startDateTimeAsync(routeLogId).await().toDateTime())


    suspend fun getEndDateTime(routeLogId: Long)=
        Summary(END_DATETIME_LABEL, endDateTimeAsync(routeLogId).await().toDateTime())

    private fun startDateTimeAsync(routeLogId: Long) = CoroutineScope(Dispatchers.IO).async {
        Timestamp.valueOf(getRouteLog(routeLogId).startAt).time
    }
    private fun endDateTimeAsync(routeLogId: Long) = CoroutineScope(Dispatchers.IO).async {
        Timestamp.valueOf(getRouteLogDetails(routeLogId).last().timestamp).time
    }

    private fun getRouteLog(routeLogId: Long) = db.getRouteLogDao().getLog(routeLogId)

    private fun getRouteLogDetails(routeLogId: Long) = db.getRouteLogDetailDao().getDetails(routeLogId)

    private fun getRouteByRouteLogId(routeId: Long) = db.getRouteDao().getRoute(routeId)

}