package com.android.weatherapp.weather

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.android.weatherapp.R
import com.android.weatherapp.city.CityRepository
import com.android.weatherapp.city.CityViewModel
import com.android.weatherapp.city.CityViewModelFactory
import com.android.weatherapp.city.db.CityDatabase
import com.android.weatherapp.util.ResponseCode
import kotlinx.android.synthetic.main.weather_fragment.*

class WeatherFragment : Fragment() {

    companion object {
        fun newInstance() = WeatherFragment()
    }

    private val weatherViewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(
            WeatherRepository()
        )
    }
    private val args: WeatherFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        args.city?.let {
            activity?.title = it.cityName

            weatherViewModel.getTodayWeather(it.latitude, it.longitude)
        }
        viewModelObservers()
    }

    private fun viewModelObservers() {
        weatherViewModel.progressVisibility.observe(viewLifecycleOwner, Observer {
            if (it)
                pbLoading.visibility = View.VISIBLE
            else
                pbLoading.visibility = View.GONE
        })
        weatherViewModel.obsResponseCode.observe(viewLifecycleOwner, Observer {
            if (it == ResponseCode.FAILURE) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.unable_to_get_data),
                    Toast.LENGTH_LONG
                ).show()
            }

        })
        weatherViewModel.obsWeatherResponse.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                tvTempMinMax.text = getString(
                    R.string.max_temp,
                    it.main.tempMax.toString()
                ) + getString(R.string.min_temp, it.main.tempMin.toString())
                tvTemp.text = getString(R.string.temp, it.main.temp.toString())
                tvFeelsLike.text = getString(R.string.feels_like, it.main.feelsLike.toString())
                tvWeather.text = it.weather[0].main
                tvHumidity.text = getString(R.string.humidity_value, it.main.humidity.toString())
                tvWindSpeed.text = getString(R.string.wind_speed_value, it.wind.speed.toString())
            }
        })
    }

}