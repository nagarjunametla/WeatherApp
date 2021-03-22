package com.android.weatherapp.city.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.weatherapp.city.model.City

@Database(entities = [City::class], version = 1, exportSchema = false)
abstract class CityDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao

    companion object {
        @Volatile
        private var INSTANCE: CityDatabase? = null

        fun getDatabase(context: Context): CityDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CityDatabase::class.java,
                    "city_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}