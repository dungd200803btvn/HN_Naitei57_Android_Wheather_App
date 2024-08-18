package com.sun.weather.screen.favourite
import com.sun.weather.data.model.FavouriteLocation

interface FavouriteContract {
    interface View {
        fun onGetFavoriteListSuccess(favoriteList: List<FavouriteLocation>)
    }

    interface Presenter {
        fun getAllFavorite()

        fun removeFavoriteItem(id: Long)
    }
}
