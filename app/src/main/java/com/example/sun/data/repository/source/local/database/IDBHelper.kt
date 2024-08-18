package com.example.sun.data.repository.source.local.database

import com.example.sun.data.model.FavouriteLocation

interface IDBHelper {
    fun insertFavoriteWeather(favouriteLocation: FavouriteLocation): Long

    fun getAllFavorite(): List<FavouriteLocation>

    fun removeFavoriteItem(id: Long): Int
}
