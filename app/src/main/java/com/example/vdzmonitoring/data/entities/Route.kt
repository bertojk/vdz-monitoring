package com.example.vdzmonitoring.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Route (
    @PrimaryKey(autoGenerate = false)
    var routeId: Long = 0,
    var routeName: String = "",
    var latSource: Double = 0.0,
    var longSource: Double = 0.0,
    var latDest: Double = 0.0,
    var longDest: Double = 0.0,
    var createdAt: String = "",
    var createdBy: String = "",
    var updatedAt: String = "",
    var updatedBy: String = "",
    var isActive: Boolean = true
)