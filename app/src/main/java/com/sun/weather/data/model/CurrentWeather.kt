package com.sun.weather.data.model
import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    @SerializedName("main")
    var main: Main,
    @SerializedName("weather")
    var weathers: List<Weather>,
    @SerializedName("wind")
    var wind: Wind,
    @SerializedName("clouds")
    var clouds: Clouds,
    @SerializedName("coord")
    var coord: Coord,
    @SerializedName("dt")
    var dt: Long,
    @SerializedName("name")
    var nameCity: String,
    @SerializedName("sys")
    var sys: Sys,
    var day: String = "",
    var iconWeather: String = "",
)

data class Main(
    @SerializedName("temp")
    var currentTemperature: Double,
    @SerializedName("humidity")
    var humidity: Int,
    @SerializedName("temp_min") var tempMin: Double,
    @SerializedName("temp_max") var tempMax: Double,
)

data class Weather(
    @SerializedName("icon")
    var iconWeather: String,
    @SerializedName("main")
    var main: String,
    @SerializedName("description")
    var description: String,
)

data class Wind(
    @SerializedName("speed")
    var windSpeed: Double,
)

data class Clouds(
    @SerializedName("all")
    var percentCloud: Int,
)

data class Coord(
    @SerializedName("lon")
    var lon: Double,
    @SerializedName("lat")
    var lat: Double,
)

data class Sys(
    @SerializedName("country")
    var country: String,
)

object CurrentWeatherEntry {
    private const val COLUMN_TEMP = "temp"
    private const val COLUMN_HUMIDITY = "humidity"
    private const val COLUMN_TEMP_MIN = "temp_min"
    private const val COLUMN_TEMP_MAX = "temp_max"
    private const val COLUMN_WEATHER_DESCRIPTION = "description"
    private const val COLUMN_WIND_SPEED = "wind_speed"
    private const val COLUMN_CLOUD_PERCENT = "cloud_percent"
    private const val COLUMN_CITY_NAME = "city_name"
    private const val COLUMN_COUNTRY_NAME = "country_name"
    private const val COLUMN_DAY = "day"
    private const val COLUMN_ICON_WEATHER = "icon_weather"
}
