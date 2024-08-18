package com.sun.weather.screen.detail

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.sun.weather.data.model.HourlyForecast
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
                        weatherRepository.saveWeeklyForecastLocal(data)
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
            object : OnResultListener<HourlyForecast> {
                override fun onSuccess(data: HourlyForecast) {
                    handler.post {
                        weatherRepository.saveHourlyForecastLocal(data)
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

    override fun loadWeeklyForecastFromLocal(cityName: String) {
        weatherRepository.getWeeklyForecastLocal(
            object : OnResultListener<WeeklyForecast> {
                override fun onSuccess(data: WeeklyForecast) {
                    handler.post {
                        Log.d("LCD", "Loaded weekly data from local: $data")
                        view?.onGetWeeklyForecastSuccess(data)
                    }
                }

                override fun onError(exception: Exception?) {
                    handler.post {
                        view?.onError(exception.toString())
                        Log.d("LCD", "Loaded hourly data from local: $exception")
                    }
                }
            },
            cityName,
        )
    }

    override fun loadHourlyForecastFromLocal(cityName: String) {
        weatherRepository.getHourlyForecastLocal(
            object : OnResultListener<HourlyForecast> {
                override fun onSuccess(data: HourlyForecast) {
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
