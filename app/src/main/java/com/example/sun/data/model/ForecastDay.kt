package com.example.sun.data.model

import com.google.gson.annotations.SerializedName

data class ForecastDay(
    @SerializedName("cod")
    var cod: String = "",
    @SerializedName("message")
    var message: Int = 0,
    @SerializedName("cnt")
    var cnt: Int = 0,
    @SerializedName("list")
    var forecastList: List<Forecast1Day> = listOf(),
    @SerializedName("city")
    val city: City = City(),
)

data class City(
    @SerializedName("name")
    var cityName: String = "",
    @SerializedName("country")
    var countryName: String = "",
)

data class Forecast1Day(
    @SerializedName("dt")
    var dt: Long = 0L,
    @SerializedName("main")
    var main: Main = Main(),
    @SerializedName("weather")
    var weather: List<Weather> = listOf(),
    @SerializedName("clouds")
    var clouds: Clouds = Clouds(),
    @SerializedName("wind")
    var wind: Wind = Wind(),
    @SerializedName("visibility")
    var visibility: Int = 0,
    @SerializedName("pop")
    var pop: Double = 0.0,
    @SerializedName("sys")
    var sys: Sys = Sys(),
    @SerializedName("dt_txt")
    var dtTxt: String = "",
) {
    data class Main(
        @SerializedName("temp")
        var temp: Double = 0.0,
        @SerializedName("feels_like")
        var feelsLike: Double = 0.0,
        @SerializedName("temp_min")
        var tempMin: Double = 0.0,
        @SerializedName("temp_max")
        var tempMax: Double = 0.0,
        @SerializedName("pressure")
        var pressure: Int = 0,
        @SerializedName("sea_level")
        var seaLevel: Int = 0,
        @SerializedName("grnd_level")
        var grndLevel: Int = 0,
        @SerializedName("humidity")
        var humidity: Int = 0,
        @SerializedName("temp_kf")
        var tempKf: Double = 0.0,
    )

    data class Weather(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("main")
        var main: String = "",
        @SerializedName("description")
        var description: String = "",
        @SerializedName("icon")
        var icon: String = "",
    )

    data class Clouds(
        @SerializedName("all")
        var all: Int = 0,
    )

    data class Wind(
        @SerializedName("speed")
        var speed: Double = 0.0,
        @SerializedName("deg")
        var deg: Int = 0,
        @SerializedName("gust")
        var gust: Double = 0.0,
    )

    data class Sys(
        @SerializedName("pod")
        var pod: String = "",
    )
}

data class DetailWeatherData(
    var day: String = "",
    var status: String = "",
    var maxTemp: String = "",
    var minTemp: String = "",
    var iconWeather: String = "",
)
