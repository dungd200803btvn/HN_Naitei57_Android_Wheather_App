package com.example.sun.data.repository.source.local

import com.example.sun.data.repository.source.WeatherDataSource
import com.example.sun.utils.base.SharedPrefManager

class LocalDataSourceImpl : WeatherDataSource.Local {
    override fun getSelectedLocation(key: String): String {
        return SharedPrefManager.getString(key, "") ?: ""
    }

    companion object {
        private var instance: LocalDataSourceImpl? = null

        fun getInstance() = instance ?: LocalDataSourceImpl().also { instance = it }
    }
}
