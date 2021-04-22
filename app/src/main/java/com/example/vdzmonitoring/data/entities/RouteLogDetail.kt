package com.example.vdzmonitoring.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RouteLogDetail(
    @PrimaryKey(autoGenerate = true)
    var routeLogDetailId: Long = 0,
    var routeLogId: Long = 0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var speed: Int = 0,
    var timestamp: String = ""
)