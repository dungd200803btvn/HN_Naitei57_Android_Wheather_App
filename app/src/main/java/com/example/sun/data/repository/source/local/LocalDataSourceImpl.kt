package com.example.sun.data.repository.source.local

import com.example.sun.data.model.CurrentWeather
import com.example.sun.data.repository.source.CurrentWeatherDataSource
import com.example.sun.data.repository.source.remote.OnResultListener

class LocalDataSourceImpl : CurrentWeatherDataSource.Local {
    override fun getCurrentWeatherLocal(listener: OnResultListener<List<CurrentWeather>>) {
    }

    companion object {
        private var instance: LocalDataSourceImpl? = null

        fun getInstance() = instance ?: LocalDataSourceImpl().also { instance = it }
    }
}
