package com.android.weatherapp.network

import com.android.weatherapp.network.AppConstants.BASE_URL
import com.android.weatherapp.network.AppConstants.CONNECT_TIMEOUT
import com.android.weatherapp.network.AppConstants.READ_TIMEOUT
import com.android.weatherapp.network.AppConstants.WRITE_TIMEOUT
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitUtil {

    fun getInstance(): ApiInterface {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MINUTES)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        httpClient.addInterceptor(logging)

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .baseUrl(BASE_URL)
            .build()

        return retrofit.create(ApiInterface::class.java);
    }
}