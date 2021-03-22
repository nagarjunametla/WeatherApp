package com.android.weatherapp.city

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.weatherapp.R
import com.android.weatherapp.city.adapter.CityAdapter
import com.android.weatherapp.city.db.CityDatabase
import com.android.weatherapp.city.model.City
import com.android.weatherapp.network.AppConstants.KEY_CITY
import kotlinx.android.synthetic.main.cities_fragment.*

class CitiesFragment : Fragment() {

    private val cityViewModel: CityViewModel by viewModels {
        CityViewModelFactory(
            CityRepository(
                CityDatabase.getDatabase(requireContext()).cityDao()
            )
        )
    }
    private var cityAdapter: CityAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cities_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.title = getString(R.string.cities)
        cityAdapter = CityAdapter(requireContext(), object : CityAdapter.OnItemClickListener {
            override fun onItemClick(city: City) {
                findNavController().navigate(CitiesFragmentDirections.navigateToWeather(city))
            }

            override fun onRemoveClick(city: City) {
                cityViewModel.delete(city.cityName)
            }
        })
        rvCities.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rvCities.adapter = cityAdapter
        val city = findNavController().currentBackStackEntry?.savedStateHandle?.get<City>(KEY_CITY)
        city?.let {
            cityViewModel.insert(it)
        }

        cityViewModel.cities.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                tvInfo.visibility = View.VISIBLE
                rvCities.visibility = View.GONE
            } else {
                tvInfo.visibility = View.GONE
                rvCities.visibility = View.VISIBLE
                cityAdapter?.updateData(it)
            }
        })

        fabLocation.setOnClickListener {
            findNavController().navigate(CitiesFragmentDirections.homeToLocation())
        }
    }


}