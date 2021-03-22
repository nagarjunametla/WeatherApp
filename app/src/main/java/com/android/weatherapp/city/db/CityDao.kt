package com.android.weatherapp.city.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.weatherapp.city.model.City

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: City)

    @Query("SELECT * FROM City")
    fun getCities(): LiveData<List<City>>

    @Query("DELETE FROM City WHERE cityName=:cityName")
    suspend fun deleteCity(cityName: String)
}