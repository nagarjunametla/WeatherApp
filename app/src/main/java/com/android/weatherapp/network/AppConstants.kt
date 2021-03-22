package com.android.weatherapp.network

object AppConstants {
    const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
    const val APP_ID="fae7190d7e6433ec3a45285ffcf55c86"
    const val VALUE_METRIC="metric"
    const val CONNECT_TIMEOUT = 1L
    const val READ_TIMEOUT = 120L
    const val WRITE_TIMEOUT = 120L

    const val REQUEST_TODAY_WEATHER = "weather"
    const val REQUEST_WEATHER_FORECAST = "forecast"

    const val PARAM_LATITUDE = "lat"
    const val PARAM_LONGITUDE = "lon"
    const val PARAM_APP_ID = "appid"
    const val PARAM_UNITS="units"

    const val KEY_CITY="city"
}