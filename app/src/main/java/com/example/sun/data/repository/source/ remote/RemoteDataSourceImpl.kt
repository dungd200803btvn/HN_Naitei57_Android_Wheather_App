package com.example.sun.data.repository.source.remote
import com.example.sun.data.model.CurrentWeather
import com.example.sun.data.repository.source.CurrentWeatherDataSource
import com.example.sun.data.repository.source.remote.fetchjson.ApiManager
import com.example.sun.utils.base.Constant
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RemoteDataSourceImpl : CurrentWeatherDataSource.Remote {
    override fun getCurrentWeather(
        listener: OnResultListener<CurrentWeather>,
        city: String,
    ) {
        val apiManager = ApiManager.newInstance(threadPoolSize = 4) // Example thread pool size
        val urlString = Constant.BASE_URL + "weather?q=$city&appid=${Constant.BASE_API_KEY}"
        apiManager.executeApiCall(
            urlString,
            object : OnResultListener<CurrentWeather> {
                override fun onSuccess(data: CurrentWeather) {
                    val gson = Gson()
                    val weather = gson.fromJson(data.toString(), CurrentWeather::class.java)
                    val date = Date(weather.dt * 1000L)
                    weather.day = SimpleDateFormat("EEEE yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date)
                    weather.iconWeather = Constant.BASE_ICON_URL + weather.iconWeather + "@2x.png"
                    listener.onSuccess(weather)
                }

                override fun onError(exception: Exception?) {
                    listener.onError(exception)
                }
            },
        )
    }
}
