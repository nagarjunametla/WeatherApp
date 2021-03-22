package com.android.weatherapp.city

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.weatherapp.R
import com.android.weatherapp.city.model.City
import com.android.weatherapp.network.AppConstants.KEY_CITY
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.location_bottom_sheet.*
import java.io.IOException
import java.util.*


class MarkCityFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    companion object {
        fun newInstance() = MarkCityFragment()
    }

    private val REQUEST_CHECK_SETTINGS: Int = 100
    private val LOCATION_PERMISSION_CODE: Int = 101
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var googleMap: GoogleMap? = null
    private var latitude = 0.0
    private var longitude = 0.0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.mark_city_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.title = getString(R.string.mark_a_city)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = FusedLocationProviderClient(requireContext())
        btnMarkLocation.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                KEY_CITY,
                City(tvCity.text.toString().trim(), latitude, longitude)
            )
            findNavController().popBackStack()
        }
    }

    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    LOCATION_PERMISSION_CODE
                )
                return;
            }
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        if (arePermissionsChecked()) {
            configureMap()
        } else {
            requestLocationPermission()
        }
        googleMap?.setOnMarkerDragListener(this)
    }

    @SuppressLint("MissingPermission")
    private fun configureMap() {
        googleMap?.isMyLocationEnabled = true
        googleMap?.uiSettings?.isMyLocationButtonEnabled = true
        googleMap?.uiSettings?.isZoomControlsEnabled = true
        getCurrentLocation()
    }

    private fun getCurrentLocation() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = (10 * 1000).toLong()
        locationRequest.fastestInterval = 2000
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()
        val result = LocationServices.getSettingsClient(requireActivity())
            .checkLocationSettings(locationSettingsRequest)
        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                if (response!!.locationSettingsStates.isLocationPresent) {
                    getLastLocation()
                }
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvable = exception as ResolvableApiException
                        resolvable.startResolutionForResult(
                            requireActivity(),
                            REQUEST_CHECK_SETTINGS
                        )
                    } catch (e: IntentSender.SendIntentException) {
                    } catch (e: ClassCastException) {
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {

        fusedLocationClient.lastLocation
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful && task.result != null) {
                    val mLastLocation = task.result
                    setLocation(mLastLocation!!.latitude, mLastLocation.longitude)
                } else {
                    Toast.makeText(requireContext(), "No current location found", Toast.LENGTH_LONG)
                        .show()
                }
            }
    }

    private fun setLocation(lat: Double, lng: Double) {
        googleMap?.clear()
        var address = "Current address"
        val gcd = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address>
        try {
            this.latitude = lat
            this.longitude = lng
            addresses = gcd.getFromLocation(lat, lng, 1)
            if (addresses.isNotEmpty()) {
                address = addresses[0].getAddressLine(0)
                val city: String? = addresses[0].locality
                if (city == null) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.unable_to_fetch_city_try_again),
                        Toast.LENGTH_LONG
                    ).show()
                    tvCity.text = ""
                } else
                    tvCity.text = city
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val icon = BitmapDescriptorFactory.fromBitmap(
            BitmapFactory.decodeResource(
                this.resources,
                R.drawable.pin
            )
        )
        googleMap?.addMarker(
            MarkerOptions()
                .position(LatLng(lat, lng))
                .title("Location")
                .snippet("Near $address")
                .icon(icon)
                .draggable(true)
        )
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(lat, lng))
            .zoom(12f)
            .build()
        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun arePermissionsChecked(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_CODE && grantResults.isNotEmpty()) {
            configureMap()
        } else {
            requestLocationPermission()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }


    override fun onMarkerDragEnd(marker: Marker?) {
        val updatedLatLng = marker?.position
        updatedLatLng?.let {
            setLocation(updatedLatLng.latitude, updatedLatLng.longitude)
        }


    }

    override fun onMarkerDragStart(marker: Marker?) {
    }

    override fun onMarkerDrag(marker: Marker?) {
    }

}