package com.example.sun.screen.home

import android.os.Handler
import android.os.Looper
import com.example.sun.data.model.CurrentWeather
import com.example.sun.data.repository.WeatherRepository
import com.example.sun.data.repository.source.remote.OnResultListener
import java.lang.Exception

class HomePresenter(
    private val weatherRepository: WeatherRepository,
) : HomeContract.Presenter {
    private var view: HomeContract.View? = null
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 60000L // 1 minute

    fun setView(view: HomeContract.View) {
        this.view = view
    }

    override fun getCurrentWeather(city: String) {
        weatherRepository.getCurrentWeather(
            object : OnResultListener<CurrentWeather> {
                override fun onSuccess(data: CurrentWeather) {
                    handler.post {
                        view?.onGetCurrentWeatherSuccess(data)
                    }
                }

                override fun onError(exception: Exception?) {
                    handler.post {
                        view?.onError(exception?.message ?: "Đã xảy ra lỗi")
                    }
                }
            },
            city,
        )
    }

    override fun getCurrentLocationWeather(
        latitude: Double,
        longitude: Double,
    ) {
        val runnable =
            object : Runnable {
                override fun run() {
                    weatherRepository.getCurrentLocationWeather(
                        object : OnResultListener<CurrentWeather> {
                            override fun onSuccess(data: CurrentWeather) {
                                handler.post {
                                    view?.onGetCurrentLocationWeatherSuccess(data)
                                }
                            }

                            override fun onError(exception: Exception?) {
                                handler.post {
                                    view?.onError(exception.toString())
                                }
                            }
                        },
                        latitude,
                        longitude,
                    )
                    handler.postDelayed(this, 60 * 1000) // 60 minutes in milliseconds
                }
            }

        handler.post(runnable) // Start the initial execution
    }
}
