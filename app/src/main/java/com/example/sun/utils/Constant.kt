package com.example.sun.utils.base
import com.example.weather.BuildConfig

object Constant {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    val BASE_API_KEY: String
        get() = BuildConfig.BASE_API_KEY
    const val BASE_LANGUAGE_VI = "&lang=vi"
    const val BASE_LANGUAGE = "&language=en-US"
    const val BASE_ICON_URL = "https://openweathermap.org/img/wn/"
}
