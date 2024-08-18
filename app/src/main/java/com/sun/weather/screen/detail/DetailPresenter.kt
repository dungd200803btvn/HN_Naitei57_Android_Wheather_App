package com.sun.weather.screen.detail

import android.os.Handler
import android.os.Looper
import com.sun.weather.data.model.HourlyForcast
import com.sun.weather.data.model.WeeklyForecast
import com.sun.weather.data.repository.source.WeatherRepository
import com.sun.weather.data.repository.source.remote.OnResultListener

class DetailPresenter(private val weatherRepository: WeatherRepository) : DetailContract.Presenter {
    private var view: DetailContract.View? = null
    private val handler = Handler(Looper.getMainLooper())

    fun setView(view: DetailContract.View) {
        this.view = view
    }

    override fun getWeeklyForecast(cityName: String) {
        weatherRepository.getWeeklyForecast(
            object : OnResultListener<WeeklyForecast> {
                override fun onSuccess(data: WeeklyForecast) {
                    handler.post {
                        view?.onGetWeeklyForecastSuccess(data)
                    }
                }

                override fun onError(exception: Exception?) {
                    handler.post {
                        view?.onError(exception.toString())
                    }
                }
            },
            cityName,
        )
    }

    override fun getHourlyForecast(cityName: String) {
        weatherRepository.getHourlyForecast(
            object : OnResultListener<HourlyForcast> {
                override fun onSuccess(data: HourlyForcast) {
                    handler.post {
                        view?.onGetHourlyForecastSuccess(data)
                    }
                }

                override fun onError(exception: Exception?) {
                    handler.post {
                        view?.onError(exception.toString())
                    }
                }
            },
            cityName,
        )
    }
}
