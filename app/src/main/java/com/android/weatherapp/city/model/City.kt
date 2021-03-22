package com.android.weatherapp.city.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class City(
    @PrimaryKey val cityName: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable