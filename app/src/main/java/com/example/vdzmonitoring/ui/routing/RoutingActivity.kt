package com.example.vdzmonitoring.ui.routing

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.StrictMode
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.vdzmonitoring.R
import com.example.vdzmonitoring.ui.home.HomeActivity
import com.example.vdzmonitoring.ui.summarize.SummarizeActivity
import com.example.vdzmonitoring.util.*
import kotlinx.android.synthetic.main.activity_routing.*
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon

private const val DEVELOPMENT_MODE = 1234
private const val PRODUCTION_MODE = 5678

private const val APPLICATION_MODE = DEVELOPMENT_MODE

class RoutingActivity :
    AppCompatActivity(),
    KodeinAware,
    LocationListener{

    override val kodein by kodein()

    private val factory: RoutingViewModelFactory by instance()

    private lateinit var viewModel: RoutingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routing)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // Initialize ViewModel
        viewModel = ViewModelProviders.of(this, factory)
            .get(RoutingViewModel::class.java)

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackButtonPressed()
            }
        })

        begin()
    }

    private fun begin() {
        // 1. Initial ViewModel, Repository etc
        preInit()

        // 2. Initialize Map. This include points for User, VDZ and destination
        initializeMap()

        // 3. For update location of User
        updateMyLocation()

        // 4. Start Simulator
        if(APPLICATION_MODE == DEVELOPMENT_MODE) startSimulator()
    }
    
    private fun onBackButtonPressed() {
        val builder = AlertDialog.Builder(this).apply { 
            setMessage("Are you sure want to end this routing?")
            setPositiveButton("YES") { _, _ ->
                changeActivity(SummarizeActivity::class.java, true, param =
                    TAG_CURRENT_ROUTE_LOG_ID to viewModel.currentRouteLogId)
            }
            setNegativeButton("NO") { _,_ ->

            }
        }
        builder.create().show()
    }

    private fun startSimulator() {
        GlobalScope.launch {
            val road = viewModel.getRoad()
            val simulator = Simulator(viewModel.myRoute, road)
            val delaytime = simulator.delaytime
            val steps = simulator.steps.toList()
            for(i in steps.indices) {
                delay(delaytime)
                val speed = when(i == 0) {
                    true -> 0.0
                    else -> steps[i-1].distanceToAsDouble(steps[i])
                }.times(1000L/delaytime).times(3.6)
                viewModel.updateMyLocationBySimulator(steps[i], speed)
            }
        }
    }

    // Preinit function is a function for pre initialization in Android system
    @SuppressLint("MissingPermission")
    private fun preInit() {
        // Load Configuration
        val ctx = this.applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))

        // Set policy for access OSM Server
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // set location manager for getting user location
        try {
            val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1L, 1F, this)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1L, 1F, this)
            val criteria = Criteria().apply {
                accuracy = Criteria.ACCURACY_FINE
                powerRequirement = Criteria.POWER_LOW
            }

            val location = locationManager.getLastKnownLocation(
                locationManager.getBestProvider(criteria, true)!!
            )!!

            viewModel.updateMyLocation(location)
            viewModel.setMyRoute(intent.extras!!.getLong(TAG_ROUTE_ID_SELECTED))
        } catch (e: Exception) {
            routing_mapView.snackbar("ERROR OCCURRED!!")
        }
    }

    private fun initializeMap() {
        routing_mapView.apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setZoomRounding(true)
            setMultiTouchControls(true)
            CoroutineScope(Dispatchers.IO).launch {
                val road = viewModel.getRoadsAsync()
                overlays.add(generateRoad(road.await()))
                overlays.addAll(generateVDZList(viewModel.vdzObservers))
            }
            viewModel.myPosition.value?.let {
                overlays.add(generateMyPositionMarker(it))
            }
            controller.apply{
                setZoom(18.0)
                setCenter(viewModel.myPosition.value)
            }
            invalidate()
        }
    }

    private fun updateMyLocation() {
        viewModel.apply {
            myPosition.observe(this@RoutingActivity, Observer {
                routing_mapView.apply {
                    val previousLocation = overlays.firstOrNull{ overlay ->
                        (overlay as Marker).title == TAG_MY_LOCATION
                    }
                    previousLocation?.run {
                        val a = this as Marker
                        overlays[overlays.indexOf(a)] =  generateMyPositionMarker(it)
                        removeAllVDZOnMap()
                        overlays.addAll(generateVDZList(viewModel.vdzObservers))
                        controller.setCenter(it)
                        invalidate()
                    }

                }
                val d =
                    viewModel.vdzObservers.sortedBy {
                        it.distance
                    }.firstOrNull {
                        it.status != VDZ_STATUS_OUTSIDE_PASS_VDZ
                    }

                val distanceToDest = myPosition.value!!.distanceToAsDouble(getDestination())

                if(d != null)
                    routing_distance_text.text = getString(R.string.distance_text, d.distance.toInt(), d.id)
                else
                    routing_distance_text.text = "Destination is in ${distanceToDest} meters away"
                if(distanceToDest < 0.01)
                    onFinish()
            })

            mySpeed.observe(this@RoutingActivity, Observer {
                speed.text = getString(R.string.speed_format,it)
            })

            isRoutingFinish.observe(this@RoutingActivity, Observer {
                if(it) changeActivity(SummarizeActivity::class.java, param =
                    TAG_CURRENT_ROUTE_LOG_ID to currentRouteLogId)
            })
        }
    }

    private fun removeAllVDZOnMap() {
        val allVDZ = routing_mapView.overlays.filter {
            it is Polygon && it.title.contains(TAG_VDZ, true)
        }
        if(allVDZ.isNotEmpty())
            routing_mapView.overlays.removeAll(allVDZ)
    }

    override fun onLocationChanged(location: Location?) {
        if(APPLICATION_MODE == PRODUCTION_MODE) viewModel.updateMyLocation(location)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // onRequestPermissionResult is a override function of Activity for responding permissions from User
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
                changeActivity(HomeActivity::class.java)
            }
        }

        if (requestCode == WRITE_STORAGE_PERMISSION_CODE) {
            if (!(grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
                changeActivity(HomeActivity::class.java)
            }
        }
    }
}
