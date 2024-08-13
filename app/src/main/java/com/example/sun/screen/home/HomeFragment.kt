package com.example.sun.screen.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.sun.data.model.CurrentWeather
import com.example.sun.data.model.FavouriteLocation
import com.example.sun.data.repository.WeatherRepository
import com.example.sun.data.repository.source.local.LocalDataSourceImpl
import com.example.sun.data.repository.source.remote.RemoteDataSourceImpl
import com.example.sun.screen.detail.DetailFragment
import com.example.sun.screen.search.SearchFragment
import com.example.sun.utils.base.BaseFragment
import com.example.sun.utils.ext.replaceFragment
import com.example.weather.R
import com.example.weather.databinding.FragmentHomeBinding
import com.google.gson.Gson

class HomeFragment : BaseFragment<FragmentHomeBinding>(), HomeContract.View {
    private var homePresenter: HomePresenter? = null
    private val myTag = "HomeFragment"
    private lateinit var data: CurrentWeather
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var cityNameResultSearch: String = ""

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
        getCurrentLocationWeather()
//        loadLocationFromPreferences("location_prefs")
//        if (latitude != 0.0 && longitude != 0.0) {
//            homePresenter?.getCurrentLocationWeather(latitude, longitude)
//            homePresenter?.getCurrentWeather(cityNameResultSearch)
//        } else {
//            if (::data.isInitialized) {
//                updateUIWithCurrentWeather(data)
//            }
//        }
        viewBinding.tvLocation.setOnClickListener {
            replaceFragment(R.id.fl_container, SearchFragment.newInstance(), true)
        }
        viewBinding.icLocation.setOnClickListener {
            getCurrentLocationWeather()
        }
        viewBinding.icArrowDown.setOnClickListener {
            loadLocationFromPreferences("location_prefs")
            if (latitude != 0.0 && longitude != 0.0) {
//            homePresenter?.getCurrentLocationWeather(latitude, longitude)
                Toast.makeText(requireContext(), "Search Result: $cityNameResultSearch", Toast.LENGTH_SHORT).show()
                homePresenter?.getCurrentWeather(cityNameResultSearch)
            }
        }
        viewBinding.btnForecastReport.setOnClickListener {
            saveLocationToPreferences(latitude, longitude)
            replaceFragment(R.id.fl_container, DetailFragment.newInstance(), true)
        }
        viewBinding.btnAddFavourite.setOnClickListener {
            val favouriteLocation = FavouriteLocation(data.nameCity, data.sys.country)
            saveFavouriteLocation(requireContext(), favouriteLocation)
            Toast.makeText(requireContext(), "Added to favourites", Toast.LENGTH_SHORT).show()
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

    private fun saveWeatherDataToPreferences(currentWeather: CurrentWeather) {
        val sharedPref = requireContext().getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("cityName", currentWeather.nameCity)
            putString("countryName", currentWeather.sys.country)
            putString("description", currentWeather.weather[0].description)
            putFloat("temperature", currentWeather.main.currentTemperature.toFloat())
            apply()
        }
    }

    override fun onGetCurrentLocationWeatherSuccess(currentWeather: CurrentWeather) {
        data = currentWeather
        updateUIWithCurrentWeather(data)
        saveWeatherDataToPreferences(data)
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
        val latitudeStr = sharedPref.getString("latitude1", null)
        val longitudeStr = sharedPref.getString("longitude1", null)
        if (!latitudeStr.isNullOrEmpty() && !longitudeStr.isNullOrEmpty()) {
            try {
                latitude = latitudeStr.toDouble()
                longitude = longitudeStr.toDouble()
                if (key == "location_prefs") {
                    cityNameResultSearch = sharedPref.getString("city", "") ?: ""
                }
                Log.v(myTag, "Du lieu kieu Double: latitude: $latitude, longitude: $longitude")
            } catch (e: NumberFormatException) {
                Log.e(myTag, "Lỗi chuyển đổi: ${e.message}")
            }
        } else {
            Log.w(myTag, "Giá trị latitude hoặc longitude rỗng hoặc không tồn tại")
        }
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

        fun saveFavouriteLocation(
            context: Context,
            favouriteLocation: FavouriteLocation,
        ) {
            val sharedPref = context.getSharedPreferences("favourite_locations", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()

            val locations = getFavouriteLocations(context).toMutableList()
            locations.add(favouriteLocation)

            editor.putString("favourite_locations", Gson().toJson(locations))
            editor.apply()
        }

        fun getFavouriteLocations(context: Context): List<FavouriteLocation> {
            val sharedPref = context.getSharedPreferences("favourite_locations", Context.MODE_PRIVATE)
            val json = sharedPref.getString("favourite_locations", "")
            return if (json.isNullOrEmpty()) {
                emptyList()
            } else {
                Gson().fromJson(json, Array<FavouriteLocation>::class.java).toList()
            }
        }
    }
}
