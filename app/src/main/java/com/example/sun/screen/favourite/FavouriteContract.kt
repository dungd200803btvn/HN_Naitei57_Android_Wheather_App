package com.example.sun.screen.favourite
import com.example.sun.data.model.FavouriteLocation

interface FavouriteContract {
    interface View {
        fun onGetFavoriteListSuccess(favoriteList: List<FavouriteLocation>)
    }

    interface Presenter {
        fun getAllFavorite()

        fun removeFavoriteItem(id: Long)
    }
}
