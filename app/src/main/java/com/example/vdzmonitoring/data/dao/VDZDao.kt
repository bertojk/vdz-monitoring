package com.example.vdzmonitoring.data.dao

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vdzmonitoring.data.entities.VDZ

@Dao
interface VDZDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @WorkerThread
    suspend fun insertAllVDZ(vdzList: List<VDZ>)

    @Query("SELECT * FROM vdz")
    fun getAllVDZs() : List<VDZ>

    @Query("DELETE FROM vdz")
    fun deleteAllVDZs()

}