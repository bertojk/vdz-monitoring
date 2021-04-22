package com.example.vdzmonitoring.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.vdzmonitoring.data.entities.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrInsertUser(user: User)

    @Query("SELECT * FROM user LIMIT 1")
    fun getUser() : LiveData<User>

    @Query("DELETE FROM user")
    fun deleteAllUsers()
}