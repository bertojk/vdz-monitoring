package com.example.vdzmonitoring.ui.routing

import android.location.Location
import android.os.Build
import androidx.lifecycle.*
import com.example.vdzmonitoring.data.entities.RouteLog
import com.example.vdzmonitoring.data.entities.RouteLogDetail
import com.example.vdzmonitoring.data.repositories.RouteRepository
import com.example.vdzmonitoring.data.repositories.UserRepository
import com.example.vdzmonitoring.util.now
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.asTask
import org.kodein.di.bindings.WithContext
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline
import kotlin.math.roundToInt

const val SPEED_MPS_TO_KPH = 3.6

class RoutingViewModel(
    private val userRepo: UserRepository,
    private val routeRepo: RouteRepository,
    private val currentRouteId: Long
) : ViewModel() {

    var currentRouteLogId: Long = 0L
    private var userId = userRepo.currentUser().value!!.uid
    //private var _routeId = 0

    private val _myPosition = MutableLiveData<GeoPoint>()
    val myPosition: LiveData<GeoPoint>
        get() = _myPosition

    private val _mySpeed = MutableLiveData<Double>()
    val mySpeed: LiveData<Int> = Transformations.map(_mySpeed) {
        it.times(SPEED_MPS_TO_KPH).roundToInt()
    }

    private val _isRoutingFinish = MutableLiveData<Boolean>()
    val isRoutingFinish: LiveData<Boolean>
        get() = _isRoutingFinish

    var vdzObservers = listOf<VDZObserver>()

    lateinit var myRoute: Pair<GeoPoint, GeoPoint>

    init {
//        CoroutineScope(Dispatchers.IO).launch {
//            vdzObservers = getVDZsAsync().await()
//        }
        _isRoutingFinish.postValue(false)
    }

    fun updateMyLocationBySimulator(geoPoint: GeoPoint, speed: Double) {
        viewModelScope.launch {
            _myPosition.postValue(geoPoint)
            _mySpeed.postValue(speed)
            insertRouteLogDetail(geoPoint)
            updateVDZObserver()
        }
    }

    fun updateMyLocation(location: Location?) {
        viewModelScope.launch {
            if (location != null) {
                val point = GeoPoint(location.latitude, location.longitude)
                _myPosition.value = point

                val speed = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    location.speedAccuracyMetersPerSecond.toDouble()
                } else {
                    location.speed.toDouble()
                }
                _mySpeed.postValue(speed)
                withContext(Dispatchers.IO) {
                    insertRouteLogDetail(point)
                    updateVDZObserver()
                }
            }
        }
    }

    private suspend fun insertRouteLogDetail(point: GeoPoint) {
        withContext(Dispatchers.IO) {
            val routeLogDetail = RouteLogDetail(
                routeLogId = currentRouteLogId,
                latitude = point.latitude,
                longitude = point.longitude,
                speed = mySpeed.value ?: 0,
                timestamp = now()
            )
            routeRepo.insertRouteLogDetail(routeLogDetail)
        }
    }

    private suspend fun updateVDZObserver() {
        withContext(Dispatchers.Default) {
            vdzObservers.forEach {
                it.updateVDZ(_myPosition.value!!)
            }
        }
    }

    fun setMyRoute(routeId: Long) {
        viewModelScope.launch {
            //_routeId = routeId
            withContext(Dispatchers.IO) {
                val r = routeRepo.getRoute(currentRouteId)
                myRoute =
                    GeoPoint(r.latSource, r.longSource) to
                    GeoPoint(r.latDest, r.longDest)


                val rl = RouteLog(
                    userId = userId,
                    routeId = routeId,
                    startAt = now()
                )
                currentRouteLogId =  routeRepo.insertRouteLog(rl)
            }
            vdzObservers = getVDZsAsync().await()
        }
    }

    fun getRoadsAsync() =
        CoroutineScope(Dispatchers.IO).async {
            routeRepo.getRoad(myRoute.first, myRoute.second)
        }

    private fun getVDZsAsync() =
        CoroutineScope(Dispatchers.IO).async {
            routeRepo.getAllVDZs(currentRouteId).map {
                VDZObserver(it)
            }
        }

    fun getRoad(): Polyline {
         return routeRepo.getRoad(myRoute.first, myRoute.second)
    }

    fun getDestination() = myRoute.second

    fun onFinish()  {
        viewModelScope.launch {
            _isRoutingFinish.postValue(true)
            withContext(Dispatchers.IO) {
                routeRepo.deleteAllVDZ()
            }
        }

    }
}