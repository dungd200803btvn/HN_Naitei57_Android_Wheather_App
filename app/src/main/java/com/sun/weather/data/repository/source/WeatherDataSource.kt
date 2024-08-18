package com.sun.weather.data.repository.source

import com.sun.weather.data.model.CurrentWeather
import com.sun.weather.data.model.FavouriteLocation
import com.sun.weather.data.model.HourlyForcast
import com.sun.weather.data.model.WeeklyForecast
import com.sun.weather.data.repository.source.remote.OnResultListener

interface WeatherDataSource {
    interface Local {
        fun getSelectedLocation(key: String): String

        fun getCurrentWeatherLocal(listener: OnResultListener<List<CurrentWeather>>)

        fun insertFavoriteWeather(favouriteLocation: FavouriteLocation)

        fun getAllFavorite(): List<FavouriteLocation>

        fun removeFavoriteItem(id: Long)
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
