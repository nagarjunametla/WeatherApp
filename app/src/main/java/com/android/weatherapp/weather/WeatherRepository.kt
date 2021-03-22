package com.android.weatherapp.weather

import androidx.lifecycle.LiveData
import com.android.weatherapp.network.AppConstants.APP_ID
import com.android.weatherapp.network.AppConstants.VALUE_METRIC
import com.android.weatherapp.network.RetrofitUtil
import com.android.weatherapp.util.ResponseCode
import com.android.weatherapp.weather.model.TodayWeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherRepository() {

    fun getTodayWeather(
        lat: Double,
        lng: Double,
        result: (TodayWeatherResponse?, ResponseCode?) -> Unit
    ) {
        RetrofitUtil.getInstance().getPresentWeather(lat, lng, APP_ID, VALUE_METRIC)
            .enqueue(object : Callback<TodayWeatherResponse> {
                override fun onFailure(call: Call<TodayWeatherResponse>, t: Throwable) {
                    result(null, ResponseCode.FAILURE)
                }

                override fun onResponse(
                    call: Call<TodayWeatherResponse>,
                    response: Response<TodayWeatherResponse>
                ) {
                    if (response.body() == null)
                        result(null, ResponseCode.FAILURE)
                    else
                        result(response.body(), ResponseCode.SUCCESS)
                }
            })
    }
}