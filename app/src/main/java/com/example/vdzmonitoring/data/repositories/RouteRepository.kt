package com.example.vdzmonitoring.data.repositories

import androidx.lifecycle.LiveData
import com.example.vdzmonitoring.data.database.AppDatabase
import com.example.vdzmonitoring.data.database.FirebaseAuthSource
import com.example.vdzmonitoring.data.entities.Route
import com.example.vdzmonitoring.data.entities.RouteLog
import com.example.vdzmonitoring.data.entities.RouteLogDetail
import com.example.vdzmonitoring.data.entities.VDZ
import com.example.vdzmonitoring.data.network.ApiService
import com.example.vdzmonitoring.data.network.SafeApiRequest
import org.osmdroid.bonuspack.routing.MapQuestRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline

const val API_KEY = "0MlfQ2Xz7PflGMVyXh2AwhCfOZRvvzCz"

class RouteRepository(
    private val mAuth: FirebaseAuthSource,
    private val db: AppDatabase,
    private val api: ApiService
) : SafeApiRequest() {

    suspend fun fetchRoute(): LiveData<List<Route>> {
        val routes = apiRequest { api.fetchRoutes() }!!
        db.getRouteDao().insertRoutes(routes)
        return db.getRouteDao().getRoutes()
    }

    fun getRoute(routeId: Long) = db.getRouteDao().getRoute(routeId)

    private suspend fun fetchVDZ(routeID: Long) {
        db.getVDZ().deleteAllVDZs()
        apiRequest { api.fetchVDZs(routeID)}?.let {
            db.getVDZ().insertAllVDZ(it)
        }
    }

    suspend fun getAllVDZs (routeId: Long): List<VDZ>  {
        fetchVDZ(routeId)
        return db.getVDZ().getAllVDZs()
    }

    fun getRoad(startPoint: GeoPoint, endPoint: GeoPoint) : Polyline {
        val roadManager  = MapQuestRoadManager(API_KEY)
        val road = roadManager.getRoad(arrayListOf(startPoint, endPoint))
        return RoadManager.buildRoadOverlay(road)
    }

    suspend fun insertRouteLog(routeLog: RouteLog): Long {
        val id = db.getRouteLogDao().insertRouteLog(routeLog)
        routeLog.routeLogId = id
        //apiRequest { api.putLog(mAuth.currentUser()!!.uid, id, routeLog) }
        return id
    }

    suspend fun insertRouteLogDetail(routeLogDetail: RouteLogDetail){
        val id = db.getRouteLogDetailDao().insertRouteLogDetail(routeLogDetail)
        routeLogDetail.routeLogDetailId = id
//        api.putLogDetails(
//            mAuth.currentUser()!!.uid,
//            id,
//            routeLogDetail
//        )
    }

    suspend fun getRouteLogs(uid: String) = apiRequest { api.getLogs(uid) }?.let {
        db.getRouteLogDao().insertAllRouteLogs(it)
    }


    suspend fun getRouteLogDetails(uid: String) = apiRequest { api.getLogDetails(uid) }?.let{
        db.getRouteLogDetailDao().insertAllRouteLogDetails(it)
    }

    fun deleteAllVDZ() = db.getVDZ().deleteAllVDZs()

}
