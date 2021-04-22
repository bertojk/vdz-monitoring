package com.example.vdzmonitoring.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vdzmonitoring.data.dao.*
import com.example.vdzmonitoring.data.entities.*

@Database(
    entities = [
        User::class,
        Route::class,
        VDZ::class,
        RouteLog::class,
        RouteLogDetail::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase(){

    abstract fun getUserDao(): UserDao
    abstract fun getRouteDao(): RouteDao
    abstract fun getRouteLogDao(): RouteLogDao
    abstract fun getRouteLogDetailDao(): RouteLogDetailDao
    abstract fun getVDZ(): VDZDao
    abstract fun getHistoryDao(): HistoryDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
            instance
                ?: buildDatabase(
                    context
                ).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "MyDatabase.db"
            ).build()
    }
}