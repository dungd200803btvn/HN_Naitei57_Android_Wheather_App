package com.sun.weather.screen.detail

import com.sun.weather.data.model.HourlyForcast
import com.sun.weather.data.model.WeeklyForecast

interface DetailContract {
    interface View {
        fun onGetWeeklyForecastSuccess(listForecastDay: WeeklyForecast)

        fun onGetHourlyForecastSuccess(listForecastHour: HourlyForcast)

        fun onError(e: String)
    }

    interface Presenter {
        fun getWeeklyForecast(cityName: String)

        fun getHourlyForecast(cityName: String)
    }
}
