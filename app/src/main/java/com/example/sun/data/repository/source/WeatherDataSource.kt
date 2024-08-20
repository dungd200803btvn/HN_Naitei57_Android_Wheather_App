package com.example.sun.data.repository.source

import com.example.sun.data.model.CurrentWeather
import com.example.sun.data.model.HourlyForcast
import com.example.sun.data.model.WeeklyForecast
import com.example.sun.data.repository.source.remote.OnResultListener

interface WeatherDataSource {
    interface Local {
        fun getSelectedLocation(key: String): String
    }

    interface Remote {
        fun getCurrentWeather(
            listener: OnResultListener<CurrentWeather>,
            city: String,
        )

        fun getCurrentLocationWeather(
            listener: OnResultListener<CurrentWeather>,
            latitude: Double,
            lontitude: Double,
        )

        fun getWeeklyForecast(
            listener: OnResultListener<WeeklyForecast>,
            city: String,
        )

        fun getHourlyForecast(
            listener: OnResultListener<HourlyForcast>,
            city: String,
        )
    }
}
