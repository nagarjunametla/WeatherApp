package com.android.weatherapp.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.weatherapp.city.CityRepository
import com.android.weatherapp.city.CityViewModel
import com.android.weatherapp.util.ResponseCode
import com.android.weatherapp.weather.model.TodayWeatherResponse

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    val obsWeatherResponse = MutableLiveData<TodayWeatherResponse>()
    val progressVisibility = MutableLiveData<Boolean>()
    val obsResponseCode = MutableLiveData<ResponseCode>()

    fun getTodayWeather(lat: Double, lng: Double) {
        progressVisibility.value = true
        repository.getTodayWeather(lat, lng) { todayWeatherResponse, responseCode ->
            todayWeatherResponse?.let { obsWeatherResponse.value = it }
            responseCode?.let { obsResponseCode.value = it }
            progressVisibility.value = false
        }
    }
}

class WeatherViewModelFactory(private val repository: WeatherRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}