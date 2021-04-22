package com.example.vdzmonitoring.ui.home

import androidx.room.Embedded
import androidx.room.Relation
import com.example.vdzmonitoring.data.entities.Route
import com.example.vdzmonitoring.data.entities.RouteLog

class History (
    @Embedded
    val routeLog: RouteLog? = null,

    @Relation(parentColumn = "routeId", entityColumn = "routeId")
    val route: Route? = null
)