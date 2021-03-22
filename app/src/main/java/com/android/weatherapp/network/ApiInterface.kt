package com.android.weatherapp.network

import com.android.weatherapp.network.AppConstants.PARAM_APP_ID
import com.android.weatherapp.network.AppConstants.PARAM_LATITUDE
import com.android.weatherapp.network.AppConstants.PARAM_LONGITUDE
import com.android.weatherapp.network.AppConstants.PARAM_UNITS
import com.android.weatherapp.network.AppConstants.REQUEST_TODAY_WEATHER
import com.android.weatherapp.network.AppConstants.REQUEST_WEATHER_FORECAST
import com.android.weatherapp.weather.model.TodayWeatherResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET(REQUEST_TODAY_WEATHER)
    fun getPresentWeather(
        @Query(PARAM_LATITUDE) lat: Double,
        @Query(PARAM_LONGITUDE) lon: Double,
        @Query(PARAM_APP_ID) appId: String,
        @Query(PARAM_UNITS) units:String
    ): Call<TodayWeatherResponse>

    @GET(REQUEST_WEATHER_FORECAST)
    fun getWeatherForecast(
        @Query(PARAM_LATITUDE) lat: Double,
        @Query(PARAM_LONGITUDE) lon: Double,
        @Query(PARAM_APP_ID) appId: String
    ): Call<ResponseBody>
}