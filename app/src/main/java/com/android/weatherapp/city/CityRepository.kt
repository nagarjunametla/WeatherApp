package com.android.weatherapp.city

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.android.weatherapp.city.db.CityDao
import com.android.weatherapp.city.model.City

class CityRepository(private val cityDao: CityDao) {

    val cities: LiveData<List<City>> = cityDao.getCities()

    @WorkerThread
    suspend fun insert(city: City) {
        cityDao.insertCity(city)
    }

    @WorkerThread
    suspend fun delete(cityName: String) {
        cityDao.deleteCity(cityName)
    }
}