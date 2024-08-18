package com.sun.weather.screen.detail

import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.sun.weather.R
import com.sun.weather.data.model.HourlyForcast
import com.sun.weather.data.model.WeeklyForecast
import com.sun.weather.data.repository.source.WeatherRepository
import com.sun.weather.data.repository.source.local.LocalDataSourceImpl
import com.sun.weather.data.repository.source.remote.RemoteDataSourceImpl
import com.sun.weather.databinding.FragmentDetailBinding
import com.sun.weather.utils.base.BaseFragment
import com.sun.weather.utils.ext.goBackFragment
import java.util.Date
import java.util.Locale

class DetailFragment(private val cityName: String) : BaseFragment<FragmentDetailBinding>(), DetailContract.View {
    private var detailPresenter: DetailPresenter? = null
    private lateinit var dailyAdapter: DailyAdapter
    private lateinit var hourlyAdapter: HourlyAdapter

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentDetailBinding {
        return FragmentDetailBinding.inflate(inflater)
    }

    override fun initData() {
        detailPresenter =
            DetailPresenter(
                WeatherRepository.getInstance(
                    RemoteDataSourceImpl.getInstance(),
                    LocalDataSourceImpl.getInstance(requireContext()),
                ),
            )
        detailPresenter?.setView(this)
        detailPresenter?.getWeeklyForecast(cityName)
        detailPresenter?.getHourlyForecast(cityName)
        viewBinding.tvToolbarTitle.text =
            getString(R.string.city_name_app_bar, getString(R.string.forecast_detail), cityName)
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        viewBinding.tvCurrentTime.text = currentDate
    }

    override fun initView() {
        viewBinding.toolbar.findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            goBackFragment()
        }
        hourlyAdapter = HourlyAdapter(mutableListOf())
        viewBinding.recyclerViewHourly.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = hourlyAdapter
        }
        dailyAdapter = DailyAdapter(mutableListOf())
        viewBinding.recylerViewDaily.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dailyAdapter
        }
    }

    override fun onGetWeeklyForecastSuccess(listForecastDay: WeeklyForecast) {
        dailyAdapter.updateData(listForecastDay.forecastList)
    }

    override fun onGetHourlyForecastSuccess(listForecastHour: HourlyForcast) {
        hourlyAdapter.updateData(listForecastHour.forecastList)
    }

    override fun onError(e: String) {
        Log.d(MY_TAG, "onError: $e")
    }

    companion object {
        const val MY_TAG = "DetailFragment"

        fun newInstance(cityName: String) = DetailFragment(cityName)
    }
}