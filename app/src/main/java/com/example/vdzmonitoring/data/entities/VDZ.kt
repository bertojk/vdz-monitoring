package com.example.vdzmonitoring.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class VDZ(
    @PrimaryKey(autoGenerate = false)
    @Json(name = "vdzId")
    var vdzID: Long = 0,
    @Json(name = "routeId")
    var routeID: Long = 0,
    @Json(name = "lat")
    var latitude: Double = 0.0,
    @Json(name = "long")
    var longitude: Double = 0.0
)