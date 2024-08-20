package com.example.sun.screen.detail

import com.example.sun.data.model.HourlyForcast
import com.example.sun.data.model.WeeklyForecast

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
