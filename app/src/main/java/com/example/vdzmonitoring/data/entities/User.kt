package com.example.vdzmonitoring.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class User(
    @PrimaryKey(autoGenerate = false)
    @Json(name="userID")
    var uid: String = "",
    @Json(name="email")
    var email: String? = null,
    @Json(name="password")
    var password: String? = null,
    @Json(name="firstname")
    var firstName: String? = null,
    @Json(name="lastname")
    var lastName: String? = null,
    @Json(name="phoneNo")
    var phoneNo: String? = null,
    @Json(name="createAt")
    var createdAt: String? = null,
    @Json(name="updatedAt")
    var updatedAt: String? = null,
    @Json(name="phoneMerk")
    var phoneMerk: String? = null,
    @Json(name="phoneType")
    var phoneType: String? = null,
    @Json(name="phoneIMEI")
    var phoneIMEI: String? = null
)