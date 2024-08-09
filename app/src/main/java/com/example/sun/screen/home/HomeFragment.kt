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
import com.example.sun.screen.search.SearchFragment
import com.example.sun.utils.base.BaseFragment
import com.example.sun.utils.ext.replaceFragment
import com.example.weather.R
import com.example.weather.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(), HomeContract.View {
    private var homePresenter: HomePresenter? = null
    private val myTag = "HomeFragment"
    private lateinit var data: CurrentWeather

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater)
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

        val sharedPref = requireContext().getSharedPreferences("location_prefs", Context.MODE_PRIVATE)
        val latitude = sharedPref.getFloat("latitude", 0.0f).toDouble()
        val longitude = sharedPref.getFloat("longitude", 0.0f).toDouble()
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
        val sharedPref = context?.getSharedPreferences("current_location", Context.MODE_PRIVATE)
        val latitude = sharedPref?.getString("latitude", "0.0")?.toDouble() ?: 0.0
        val longitude = sharedPref?.getString("longitude", "0.0")?.toDouble() ?: 0.0
        homePresenter?.getCurrentLocationWeather(latitude, longitude)
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}
