package com.example.sun.screen.detail

import android.os.Handler
import android.os.Looper
import com.example.sun.data.model.ForecastDay
import com.example.sun.data.repository.WeatherRepository
import com.example.sun.data.repository.source.remote.OnResultListener

class DetailPresenter(private val weatherRepository: WeatherRepository) : DetailContract.Presenter {
    private var view: DetailContract.View? = null
    private val handler = Handler(Looper.getMainLooper())

    fun setView(view: DetailContract.View) {
        this.view = view
    }

    override fun getForecastDay(
        latitude: Double,
        longitude: Double,
    ) {
        weatherRepository.getForecastDay(
            object : OnResultListener<ForecastDay> {
                override fun onSuccess(data: ForecastDay) {
                    handler.post {
                        view?.onGetForecastDaySuccess(data)
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
    }
}
