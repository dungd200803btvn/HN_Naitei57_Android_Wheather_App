package com.sun.weather.data.repository.source.local.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.sun.weather.data.model.FavouriteLocation

final class DBHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION), IDBHelper {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.apply {
            val cursor =
                rawQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name='$TABLE_FAVORITES'",
                    null,
                )
            if (cursor.count == 0) {
                execSQL(CREATE_TABLE_FAVORITES)
                Log.d(TAG, "onCreate: Bảng favorites đã được tạo.")
            } else {
                Log.d(TAG, "onCreate: Bảng favorites đã tồn tại.")
            }
            cursor.close()
        }
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int,
    ) {
        db?.apply {
            execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITES")
            onCreate(db)
        }
    }

    override fun insertFavoriteWeather(favouriteLocation: FavouriteLocation): Long {
        val db = writableDatabase
        val contentValues =
            ContentValues().apply {
                put(COLUMN_CITY_NAME, favouriteLocation.cityName)
                put(COLUMN_COUNTRY_NAME, favouriteLocation.countryName)
            }
        return db.insert(TABLE_FAVORITES, null, contentValues)
    }

    override fun getAllFavorite(): List<FavouriteLocation> {
        val db = readableDatabase
        val cursor = db.query(TABLE_FAVORITES, null, null, null, null, null, null)
        val favoriteList = mutableListOf<FavouriteLocation>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val cityName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CITY_NAME))
                val countryName =
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY_NAME))
                favoriteList.add(FavouriteLocation(id, cityName, countryName))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return favoriteList
    }

    override fun removeFavoriteItem(id: Long): Int {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(id.toString())
        return db.delete(TABLE_FAVORITES, whereClause, whereArgs)
    }

    fun checkAndCreateTable(db: SQLiteDatabase?) {
        db?.apply {
            val cursor =
                rawQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name='$TABLE_FAVORITES'",
                    null,
                )
            if (cursor.count == 0) {
                execSQL(CREATE_TABLE_FAVORITES)
                Log.d("LCD", "checkAndCreateTable: Bảng favorites đã được tạo.")
            } else {
                Log.d("LCD", "checkAndCreateTable: Bảng favorites đã tồn tại.")
            }
            cursor.close()
        }
    }

    companion object {
        private const val TAG = "DBHelper"
        private const val DATABASE_NAME = "weather.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_FAVORITES = "favorites"
        private const val COLUMN_ID = "id"
        private const val COLUMN_CITY_NAME = "cityName"
        private const val COLUMN_COUNTRY_NAME = "countryName"
        private const val CREATE_TABLE_FAVORITES = """
            CREATE TABLE $TABLE_FAVORITES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CITY_NAME TEXT,
                $COLUMN_COUNTRY_NAME TEXT
            )
        """

        private var instance: DBHelper? = null

        fun getInstance(context: Context): DBHelper {
            if (instance == null) {
                synchronized(this) {
                    instance = DBHelper(context)
                }
            }
            return instance!!
        }
    }
}
