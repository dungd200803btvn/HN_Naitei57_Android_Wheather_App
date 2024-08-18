package com.sun.weather.data.repository.source

import com.sun.weather.data.model.CurrentWeather
import com.sun.weather.data.repository.source.remote.OnResultListener

interface CurrentWeatherDataSource {
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
            latitude: Double,
            lontitude: Double,
        )
    }
}
