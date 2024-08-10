package com.example.sun.screen.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.sun.data.model.CurrentWeather
import com.example.sun.data.repository.WeatherRepository
import com.example.sun.data.repository.source.local.LocalDataSourceImpl
import com.example.sun.data.repository.source.remote.RemoteDataSourceImpl
import com.example.sun.screen.detail.DetailFragment
import com.example.sun.screen.search.SearchFragment
import com.example.sun.utils.base.BaseFragment
import com.example.sun.utils.ext.replaceFragment
import com.example.weather.R
import com.example.weather.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(), HomeContract.View {
    private var homePresenter: HomePresenter? = null
    private val myTag = "HomeFragment"
    private lateinit var data: CurrentWeather
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

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
        loadLocationFromPreferences("location_prefs")
        if (latitude != 0.0 && longitude != 0.0) {
            homePresenter?.getCurrentLocationWeather(latitude, longitude)
        } else {
            if (::data.isInitialized) {
                updateUIWithCurrentWeather(data)
            }
        }
        viewBinding.tvLocation.setOnClickListener {
            replaceFragment(R.id.fl_container, SearchFragment.newInstance(), true)
        }
        viewBinding.icLocation.setOnClickListener {
            getCurrentLocationWeather()
        }
        viewBinding.btnForecastReport.setOnClickListener {
            saveLocationToPreferences(latitude, longitude)
            replaceFragment(R.id.fl_container, DetailFragment.newInstance(), true)
        }
    }

    override fun initView() {
    }

    private fun updateUIWithCurrentWeather(currentWeather: CurrentWeather) {
        viewBinding.tvLocation.text = "${currentWeather.nameCity}, ${currentWeather.sys.country}"
        viewBinding.tvCurrentDay.text = currentWeather.day
        viewBinding.tvCurrentTemperature.text = currentWeather.main.currentTemperature.toString()
        viewBinding.tvCurrentText.text = currentWeather.weather[0].description
        viewBinding.tvCurrentPercentCloud.text = currentWeather.wind.windSpeed.toString()
        viewBinding.tvCurrentHumidity.text = currentWeather.main.humidity.toString()
        viewBinding.tvCurrentPercentCloud1.text = currentWeather.clouds.percentCloud.toString()
        Glide.with(this)
            .load(currentWeather.iconWeather)
            .into(viewBinding.ivCurrentWeather)
    }

    override fun onGetCurrentWeatherSuccess(currentWeather: CurrentWeather) {
        data = currentWeather
        updateUIWithCurrentWeather(data)
    }

    override fun onError(e: String) {
        Log.d(myTag, "onError: $e")
        Toast.makeText(context, "Lỗi: $e", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        homePresenter = null
    }

    private fun getCurrentLocationWeather() {
        loadLocationFromPreferences("current_location")
        homePresenter?.getCurrentLocationWeather(latitude, longitude)
    }

    private fun loadLocationFromPreferences(key: String) {
        val sharedPref = requireContext().getSharedPreferences(key, Context.MODE_PRIVATE)
        latitude = sharedPref.getFloat("latitude", 0.0f).toDouble()
        longitude = sharedPref.getFloat("longitude", 0.0f).toDouble()
    }

    private fun saveLocationToPreferences(
        latitude: Double,
        longitude: Double,
    ) {
        val sharedPref = requireContext().getSharedPreferences("home_fragment_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putFloat("latitude", latitude.toFloat())
            putFloat("longitude", longitude.toFloat())
            apply()
        }
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}