package com.sun.weather.screen.detail

import com.sun.weather.data.model.HourlyForecast
import com.sun.weather.data.model.WeeklyForecast

interface DetailContract {
    interface View {
        fun onGetWeeklyForecastSuccess(listForecastDay: WeeklyForecast)

        fun onGetHourlyForecastSuccess(listForecastHour: HourlyForecast)

        fun onError(e: String)
    }

    interface Presenter {
        fun getWeeklyForecast(cityName: String)

        fun getHourlyForecast(cityName: String)

        fun loadWeeklyForecastFromLocal(cityName: String)

        fun loadHourlyForecastFromLocal(cityName: String)
    }
}
