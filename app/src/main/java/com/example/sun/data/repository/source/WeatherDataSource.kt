package com.example.sun.data.repository.source

import com.example.sun.data.model.CurrentWeather
import com.example.sun.data.model.ForecastDay
import com.example.sun.data.repository.source.remote.OnResultListener

interface WeatherDataSource {
    interface Local {
        fun getCurrentWeatherLocal(listener: OnResultListener<List<CurrentWeather>>)
    }

    interface Remote {
        fun getCurrentWeather(
            listener: OnResultListener<CurrentWeather>,
            city: String,
        )

        fun getCurrentLocationWeather(
            listener: OnResultListener<CurrentWeather>,
            lat: Double,
            lon: Double,
        )

        fun getForecastDay(
            listener: OnResultListener<ForecastDay>,
            lat: Double,
            lon: Double,
        )
    }
}
