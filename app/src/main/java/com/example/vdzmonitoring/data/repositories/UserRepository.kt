package com.example.vdzmonitoring.data.repositories

import com.example.vdzmonitoring.data.database.AppDatabase
import com.example.vdzmonitoring.data.entities.User
import com.example.vdzmonitoring.data.network.ApiService
import com.example.vdzmonitoring.data.network.SafeApiRequest

class UserRepository(
    private val db: AppDatabase,
    private val api: ApiService
) : SafeApiRequest() {
    suspend fun login(email: String, password: String): User? =
        apiRequest { api.userLogIn(email, password) }

    suspend fun register(user: User, password: String): User {
        user.password = password
        return apiRequest { api.userSignUp(user) }!!
    }

    fun logout() {
        db.run {
            getUserDao().deleteAllUsers()
            getVDZ().deleteAllVDZs()
            getRouteDao().deleteAllRoutes()
            getRouteLogDetailDao().deleteAllRouteLogDetails()
            getRouteLogDao().deleteAllRouteLogs()
        }
    }

    fun currentUser() = db.getUserDao().getUser()

    suspend fun saveUser(user: User) = db.getUserDao().updateOrInsertUser(user)
}
