package com.example.sun.data.model
import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    @SerializedName("temp")
    var currentTemperature: Double = 0.0,
    @SerializedName("icon")
    var iconWeather: String = "",
    @SerializedName("speed")
    var windSpeed: Double = 0.0,
    @SerializedName("humidity")
    var humidity: Int = 0,
    @SerializedName("all")
    var percentCloud: Int = 0,
    @SerializedName("lon")
    var lon: Double = 0.0,
    @SerializedName("lat")
    var lat: Double = 0.0,
    @SerializedName("main")
    var main: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("icon")
    var day: String = "",
    @SerializedName("name")
    var nameCity: String = "",
    @SerializedName("country")
    var country: String = "",
    @SerializedName("country")
    var dt: Long = 0,
)
