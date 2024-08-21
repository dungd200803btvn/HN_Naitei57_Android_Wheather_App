package com.example.sun.data.model

import com.google.gson.annotations.SerializedName

data class HourlyForcast(
    @SerializedName("cnt") val cnt: Int,
    @SerializedName("list") val forecastList: List<HourlyForcastItem>,
    @SerializedName("city") val city: City,
)

data class City(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("coord") val coord: Coord,
    @SerializedName("country") val country: String,
)

data class HourlyForcastItem(
    @SerializedName("dt") val dt: Long,
    @SerializedName("main") val main: Main,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("clouds") val clouds: Clouds,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("dt_txt") val dtTxt: String,
    val iconWeather: String = "",
)
