package com.sun.weather.screen.favourite

import android.os.Handler
import android.os.Looper
import com.sun.weather.data.repository.source.WeatherRepository

class FavouritePresenter(private val weatherRepository: WeatherRepository) : FavouriteContract.Presenter {
    private var view: FavouriteContract.View? = null
    private val handler = Handler(Looper.getMainLooper())

    fun setView(view: FavouriteContract.View) {
        this.view = view
    }

    override fun getAllFavorite() {
        val favoriteList = weatherRepository.getAllFavorite()
        handler.post {
            view?.onGetFavoriteListSuccess(favoriteList)
        }
    }

    override fun removeFavoriteItem(id: Long) {
        weatherRepository.removeFavoriteItem(id)
        getAllFavorite()
    }
}
