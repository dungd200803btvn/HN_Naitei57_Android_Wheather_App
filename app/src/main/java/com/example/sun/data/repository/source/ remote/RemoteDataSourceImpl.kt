package com.example.sun.data.repository.source.remote
import com.example.sun.data.model.CurrentWeather
import com.example.sun.data.repository.source.CurrentWeatherDataSource
import com.example.sun.data.repository.source.remote.fetchjson.ApiManager
import com.example.sun.utils.base.Constant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RemoteDataSourceImpl : CurrentWeatherDataSource.Remote {
    private val apiManager: ApiManager = ApiManager.newInstance(threadPoolSize = 4)

    override fun getCurrentWeather(
        listener: OnResultListener<CurrentWeather>,
        city: String,
    ) {
        val urlString =
            "${Constant.BASE_URL}${Constant.WEATHER_ENDPOINT}?" +
                "${Constant.QUERY_PARAM}=$city&" +
                "${Constant.APPID_PARAM}=${Constant.BASE_API_KEY}&" +
                "${Constant.UNITS_PARAM}=${Constant.UNITS_VALUE}"
        apiManager.executeApiCall(
            urlString,
            object : OnResultListener<CurrentWeather> {
                override fun onSuccess(data: CurrentWeather) {
                    val date = Date(data.dt * 1000L)
                    data.day = SimpleDateFormat("EEEE yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date)
                    data.iconWeather = Constant.BASE_ICON_URL + data.weather[0].iconWeather + "@2x.png"
                    listener.onSuccess(data)
                }

                override fun onError(exception: Exception?) {
                    listener.onError(exception)
                }
            },
        )
    }

    override fun getCurrentLocationWeather(
        listener: OnResultListener<CurrentWeather>,
        lat: Double,
        lon: Double,
    ) {
        val urlString =
            "${Constant.BASE_URL}${Constant.WEATHER_ENDPOINT}?" +
                "${Constant.LAT_PARAM}=$lat&" +
                "${Constant.LON_PARAM}=$lon&" +
                "${Constant.APPID_PARAM}=${Constant.BASE_API_KEY}&" +
                "${Constant.UNITS_PARAM}=${Constant.UNITS_VALUE}"
        apiManager.executeApiCall(
            urlString,
            object : OnResultListener<CurrentWeather> {
                override fun onSuccess(data: CurrentWeather) {
                    val date = Date(data.dt * 1000L)
                    data.day = SimpleDateFormat("EEEE yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date)
                    data.iconWeather = Constant.BASE_ICON_URL + data.weather[0].iconWeather + "@2x.png"
                    listener.onSuccess(data)
                }

                override fun onError(exception: Exception?) {
                    listener.onError(exception)
                }
            },
        )
    }

    companion object {
        private var instance: RemoteDataSourceImpl? = null

        fun getInstance() = instance ?: RemoteDataSourceImpl().also { instance = it }
    }
}
