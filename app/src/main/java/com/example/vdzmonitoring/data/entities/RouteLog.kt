package com.example.vdzmonitoring.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RouteLog(
    @PrimaryKey(autoGenerate = true)
    var routeLogId: Long = 0,
    var userId: String = "",
    var routeId: Long = -1,
    var startAt: String = ""
)