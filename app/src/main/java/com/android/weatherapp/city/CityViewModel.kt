package com.android.weatherapp.city

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.weatherapp.city.model.City
import kotlinx.coroutines.launch

class CityViewModel(private val repository: CityRepository) : ViewModel() {

    val cities: LiveData<List<City>> = repository.cities

    fun insert(city: City) = viewModelScope.launch { repository.insert(city) }

    fun delete(cityName:String)=viewModelScope.launch { repository.delete(cityName) }
}

class CityViewModelFactory(private val repository: CityRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}