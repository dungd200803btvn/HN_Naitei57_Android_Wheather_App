package com.example.sun.screen.detail

import android.util.Log
import android.view.LayoutInflater
import com.example.sun.data.model.HourlyForcast
import com.example.sun.data.model.WeeklyForecast
import com.example.sun.data.repository.source.WeatherRepository
import com.example.sun.data.repository.source.local.LocalDataSourceImpl
import com.example.sun.data.repository.source.remote.RemoteDataSourceImpl
import com.example.sun.screen.home.HomeFragment.Companion.MY_TAG
import com.example.sun.utils.base.BaseFragment
import com.example.weather.databinding.FragmentDetailBinding

class DetailFragment(private val cityName: String) : BaseFragment<FragmentDetailBinding>(), DetailContract.View {
    private var detailPresenter: DetailPresenter? = null

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentDetailBinding {
        return FragmentDetailBinding.inflate(inflater)
    }

    override fun initData() {
        detailPresenter =
            DetailPresenter(
                WeatherRepository.getInstance(
                    RemoteDataSourceImpl.getInstance(),
                    LocalDataSourceImpl.getInstance(),
                ),
            )
        detailPresenter?.setView(this)
    }

    override fun initView() {
        // TODO setup Adapter later
    }

    override fun onGetWeeklyForecastSuccess(listForecastDay: WeeklyForecast) {
        // TODO update view later
    }

    override fun onGetHourlyForecastSuccess(listForecastHour: HourlyForcast) {
        // TODO  update view later
    }

    override fun onError(e: String) {
        Log.d(MY_TAG, e)
    }

    companion object {
        fun newInstance(cityName: String) = DetailFragment(cityName)
    }
}
