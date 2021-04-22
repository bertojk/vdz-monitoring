package com.example.vdzmonitoring

import android.app.Application
import com.example.vdzmonitoring.data.database.AppDatabase
import com.example.vdzmonitoring.data.database.FirebaseAuthSource
import com.example.vdzmonitoring.data.network.ApiService
import com.example.vdzmonitoring.data.repositories.HistoryRepository
import com.example.vdzmonitoring.data.repositories.RouteRepository
import com.example.vdzmonitoring.data.repositories.SummaryRepository
import com.example.vdzmonitoring.data.repositories.UserRepository
import com.example.vdzmonitoring.ui.auth.AuthViewModelFactory
import com.example.vdzmonitoring.ui.home.HomeViewModelFactory
import com.example.vdzmonitoring.ui.routing.RoutingActivity
import com.example.vdzmonitoring.ui.routing.RoutingViewModelFactory
import com.example.vdzmonitoring.ui.selectroute.SelectRouteViewModelFactory
import com.example.vdzmonitoring.ui.summarize.SummarizeViewModelFactory
import com.example.vdzmonitoring.util.TAG_ROUTE_ID_SELECTED
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*

class VDZMonitoringApplication : Application(), KodeinAware {

    override val kodein =  Kodein.lazy {
        import(androidXModule(this@VDZMonitoringApplication))

        bind() from singleton { FirebaseAuthSource() }
        bind() from singleton { ApiService() }
        bind() from singleton { AppDatabase(instance()) }

        bind() from singleton { UserRepository(instance(), instance()) }
        bind() from singleton { RouteRepository(instance(), instance(), instance()) }
        bind() from singleton { SummaryRepository(instance()) }
        bind() from singleton { HistoryRepository(instance()) }


        bind() from provider { AuthViewModelFactory(instance(), instance()) }
        bind() from provider { HomeViewModelFactory(instance(), instance()) }
        bind() from provider { SelectRouteViewModelFactory(instance()) }
        bind() from contexted<RoutingActivity>().provider { RoutingViewModelFactory(instance(), instance(), context) }
        bind() from provider { SummarizeViewModelFactory(instance()) }
    }

}