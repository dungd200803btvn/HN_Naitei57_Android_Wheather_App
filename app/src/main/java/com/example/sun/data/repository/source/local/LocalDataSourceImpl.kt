package com.example.sun.data.repository.source.local

import android.content.Context
import com.example.sun.data.model.CurrentWeather
import com.example.sun.data.model.FavouriteLocation
import com.example.sun.data.repository.source.WeatherDataSource
import com.example.sun.data.repository.source.local.database.DBHelper
import com.example.sun.data.repository.source.remote.OnResultListener
import com.example.sun.utils.base.SharedPrefManager

class LocalDataSourceImpl(private val dbHelper: DBHelper) : WeatherDataSource.Local {
    override fun getSelectedLocation(key: String): String {
        return SharedPrefManager.getString(key, "") ?: ""
    }

    override fun getCurrentWeatherLocal(listener: OnResultListener<List<CurrentWeather>>) {
        // TODO implement later
    }

    override fun insertFavoriteWeather(favouriteLocation: FavouriteLocation) {
        dbHelper.insertFavoriteWeather(favouriteLocation)
    }

    override fun getAllFavorite(): List<FavouriteLocation> {
        return dbHelper.getAllFavorite()
    }

    override fun removeFavoriteItem(id: Long) {
        dbHelper.removeFavoriteItem(id)
    }

    companion object {
        private var instance: LocalDataSourceImpl? = null

        fun getInstance(context: Context) =
            synchronized(this) {
                instance ?: LocalDataSourceImpl(DBHelper(context)).also { instance = it }
            }
    }
}
