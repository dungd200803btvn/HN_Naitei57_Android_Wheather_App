package com.example.sun.screen.detail

import com.example.sun.data.model.ForecastDay

interface DetailContract {
    interface View {
        fun onGetForecastDaySuccess(listForecastDay: ForecastDay)

        fun onError(e: String)
    }

    interface Presenter {
        fun getForecastDay(
            latitude: Double,
            longitude: Double,
        )
    }
}
