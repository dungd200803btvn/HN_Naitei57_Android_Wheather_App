package com.example.sun.data.model

import com.google.gson.annotations.SerializedName

data class WeeklyForecast(
    @SerializedName("city") val city: City,
    @SerializedName("cnt") val cnt: Int,
    @SerializedName("list") val forecastList: List<WeeklyForecastItem>,
)

data class WeeklyForecastItem(
    @SerializedName("dt") val dt: Long,
    @SerializedName("temp") val temp: Temp,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("speed") val speed: Double,
    @SerializedName("clouds") val clouds: Int,
)

data class Temp(
    @SerializedName("day") val day: Double,
    @SerializedName("min") val min: Double,
    @SerializedName("max") val max: Double,
    @SerializedName("night") val night: Double,
    @SerializedName("eve") val eve: Double,
    @SerializedName("morn") val morn: Double,
)
