package com.example.sun.screen.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.sun.data.model.CurrentWeather
import com.example.sun.data.repository.source.CurrentWeatherRepository
import com.example.sun.data.repository.source.local.LocalDataSourceImpl
import com.example.sun.data.repository.source.remote.RemoteDataSourceImpl
import com.example.sun.screen.LocationProvider
import com.example.sun.utils.base.BaseFragment
import com.example.weather.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(), HomeContract.View {
    private var homePresenter: HomePresenter? = null
    private val myTag = "HomeFragment"
    private lateinit var data: CurrentWeather
    private var locationProvider: LocationProvider? = null

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LocationProvider) {
            locationProvider = context
        } else {
            throw RuntimeException("$context must implement LocationProvider")
        }
    }

    override fun initData() {
        homePresenter =
            HomePresenter(
                CurrentWeatherRepository.getInstance(
                    RemoteDataSourceImpl.getInstance(),
                    LocalDataSourceImpl.getInstance(),
                ),
            )
        homePresenter?.setView(this)
        homePresenter?.getCurrentWeather("Tokyo")
    }

    override fun initView() {
        if (::data.isInitialized) {
            viewBinding.tvLocation.text = "${data.nameCity}, ${data.sys.country}"
            viewBinding.tvCurrentDay.text = data.day
            viewBinding.tvCurrentTemperature.text = data.main.currentTemperature.toString()
            viewBinding.tvCurrentText.text = data.weather[0].description
            viewBinding.tvCurrentPercentCloud.text = data.wind.windSpeed.toString()
            viewBinding.tvCurrentHumidity.text = data.main.humidity.toString()
            viewBinding.tvCurrentPercentCloud1.text = data.clouds.percentCloud.toString()
            Glide.with(this)
                .load(data.iconWeather)
                .into(viewBinding.ivCurrentWeather)
        }
        viewBinding.icLocation.setOnClickListener {
            getCurrentLocationWeather()
        }
    }

    override fun onGetCurrentWeatherSuccess(currentWeather: CurrentWeather) {
        Log.v("myTag", currentWeather.toString())
        data = currentWeather
        initView()
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
        val location = locationProvider?.retrieveCurrentLocation()
        if (location != null) {
            val latitude = location.latitude
            val longitude = location.longitude
            Log.v(myTag, "Latitude: $latitude, Longitude: $longitude")
            homePresenter?.getCurrentLocationWeather(latitude, longitude)
        } else {
            Toast.makeText(context, "Hãy bấm lại để cấp quyền vị trí", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}
