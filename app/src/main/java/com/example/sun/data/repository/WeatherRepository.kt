package com.example.sun.data.repository
import com.example.sun.data.model.CurrentWeather
import com.example.sun.data.model.ForecastDay
import com.example.sun.data.repository.source.WeatherDataSource
import com.example.sun.data.repository.source.remote.OnResultListener

class WeatherRepository(
    private val remote: WeatherDataSource.Remote,
    private val local: WeatherDataSource.Local,
) : WeatherDataSource.Local, WeatherDataSource.Remote {
    override fun getCurrentWeatherLocal(listener: OnResultListener<List<CurrentWeather>>) {
        local.getCurrentWeatherLocal(listener)
    }

    override fun getCurrentWeather(
        listener: OnResultListener<CurrentWeather>,
        city: String,
    ) {
        remote.getCurrentWeather(listener, city)
    }

    override fun getCurrentLocationWeather(
        listener: OnResultListener<CurrentWeather>,
        lat: Double,
        lon: Double,
    ) {
        remote.getCurrentLocationWeather(listener, lat, lon)
    }

    override fun getForecastDay(
        listener: OnResultListener<ForecastDay>,
        lat: Double,
        lon: Double,
    ) {
        remote.getForecastDay(listener, lat, lon)
    }

    companion object {
        private var instance: WeatherRepository? = null

        fun getInstance(
            remote: WeatherDataSource.Remote,
            local: WeatherDataSource.Local,
        ) = instance ?: WeatherRepository(remote, local).also { instance = it }
    }
}
