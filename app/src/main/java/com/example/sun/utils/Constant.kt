package com.example.sun.utils.base
import android.content.Context
import android.content.SharedPreferences
import com.example.weather.BuildConfig

object Constant {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val PRO_URL = "https://pro.openweathermap.org/data/2.5/"
    val BASE_API_KEY: String
        get() = BuildConfig.BASE_API_KEY
    val GOOGLE_MAP_API_KEY: String
        get() = BuildConfig.GOOGLE_MAPS_API_KEY
    const val BASE_ICON_URL = "https://openweathermap.org/img/wn/"
    const val WEATHER_ENDPOINT = "weather"
    const val WEEKLY_FORECAST_ENDPOINT = "forecast/daily"
    const val HOURLY_FORECAST_ENDPOINT = "forecast/hourly"
    const val APPID_PARAM = "appid"
    const val UNITS_PARAM = "units"
    const val UNITS_VALUE = "metric"
    const val LAT_PARAM = "lat"
    const val LON_PARAM = "lon"
    const val QUERY_PARAM = "q"
    const val CNT_PARAM = "cnt"
    const val FORECAST_DAY = 7
    const val FORECAST_HOUR = 24
}

object SharedPrefManager {
    private const val SHARE_PREFERENCES_NAME = "SHARE_PREFERENCES"
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(SHARE_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun getString(
        key: String,
        defaultValue: String?,
    ): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    fun putString(
        key: String,
        value: String,
    ) {
        sharedPreferences.edit().putString(key, value).apply()
    }
}
