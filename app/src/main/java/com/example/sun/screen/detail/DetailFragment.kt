package com.example.sun.screen.detail

import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sun.data.model.HourlyForcast
import com.example.sun.data.model.WeeklyForecast
import com.example.sun.data.repository.source.WeatherRepository
import com.example.sun.data.repository.source.local.LocalDataSourceImpl
import com.example.sun.data.repository.source.remote.RemoteDataSourceImpl
import com.example.sun.utils.base.BaseFragment
import com.example.sun.utils.ext.goBackFragment
import com.example.weather.R
import com.example.weather.databinding.FragmentDetailBinding
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
