package com.example.sun.screen.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.sun.data.model.CurrentWeather
import com.example.sun.data.repository.source.WeatherRepository
import com.example.sun.data.repository.source.local.LocalDataSourceImpl
import com.example.sun.data.repository.source.remote.RemoteDataSourceImpl
import com.example.sun.screen.detail.DetailFragment
import com.example.sun.screen.search.SearchFragment
import com.example.sun.utils.base.BaseFragment
import com.example.sun.utils.ext.replaceFragment
import com.example.weather.R
import com.example.weather.databinding.FragmentHomeBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

class HomeFragment : BaseFragment<FragmentHomeBinding>(), HomeContract.View {
    private var homePresenter: HomePresenter? = null
    private lateinit var currentWeather: CurrentWeather
    private var cityName: String? = null

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater)
    }

    override fun initData() {
        homePresenter =
            HomePresenter(
                WeatherRepository.getInstance(
                    RemoteDataSourceImpl.getInstance(),
                    LocalDataSourceImpl.getInstance(),
                ),
            )
        homePresenter?.setView(this)
        viewBinding.icLocation.setOnClickListener {
            requestLocationAndFetchWeather()
        }
        viewBinding.tvLocation.setOnClickListener {
            replaceFragment(R.id.fragment_container, SearchFragment.newInstance(), true)
        }
        viewBinding.icArrowDown.setOnClickListener {
            homePresenter?.getSelectedLocation(SELECTED_LOCATION)
        }
        viewBinding.btnForecastReport.setOnClickListener {
            replaceFragment(R.id.fragment_container, DetailFragment.newInstance(cityName!!), true)
        }
    }

    private fun requestLocationAndFetchWeather() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
                REQUEST_CODE,
            )
        } else {
            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
            val locationTask: Task<Location> = fusedLocationProviderClient.lastLocation
            locationTask.addOnSuccessListener { location ->
                if (location != null) {
                    homePresenter?.getCurrentLocationWeather(location.latitude, location.longitude)
                } else {
                    Toast.makeText(context, "Location is null", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateUIWithCurrentWeather(currentWeather: CurrentWeather) {
        viewBinding.tvLocation.text =
            getString(R.string.city_name, currentWeather.nameCity, currentWeather.sys.country)
        viewBinding.tvCurrentDay.text = currentWeather.day
        viewBinding.tvCurrentTemperature.text = currentWeather.main.currentTemperature.toString()
        if (currentWeather.weathers.isNotEmpty()) {
            viewBinding.tvCurrentText.text = currentWeather.weathers[0].description
        }
        viewBinding.tvCurrentPercentCloud.text = currentWeather.wind.windSpeed.toString()
        viewBinding.tvCurrentHumidity.text = currentWeather.main.humidity.toString()
        viewBinding.tvCurrentPercentCloud1.text = currentWeather.clouds.percentCloud.toString()
        Glide.with(this)
            .load(currentWeather.iconWeather)
            .into(viewBinding.ivCurrentWeather)
    }

    override fun initView() {
    }

    override fun onGetCurrentWeatherSuccess(currentWeather: CurrentWeather) {
        cityName = currentWeather.nameCity
        this.currentWeather = currentWeather
        updateUIWithCurrentWeather(currentWeather)
    }

    override fun onGetCurrentLocationWeatherSuccess(currentWeather: CurrentWeather) {
        cityName = currentWeather.nameCity
        this.currentWeather = currentWeather
        updateUIWithCurrentWeather(currentWeather)
    }

    override fun onError(e: String) {
        Log.d(MY_TAG, "onError: $e")
    }

    override fun onDestroy() {
        super.onDestroy()
        homePresenter = null
    }

    companion object {
        const val MY_TAG = "HomeFragment"
        const val REQUEST_CODE = 1000
        const val SELECTED_LOCATION = "selected_location"

        fun newInstance() = HomeFragment()
    }
}
