package com.sun.weather.screen.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.sun.weather.R
import com.sun.weather.screen.MainActivity
import com.sun.weather.screen.detail.DetailFragment
import com.sun.weather.utils.SharedPrefManager

class WeatherNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        val latitude = SharedPrefManager.getFloat(LATITUDE, DEFAULT_LATITUDE)
        val longitude = SharedPrefManager.getFloat(LONGITUDE, DEFAULT_LONGITUDE)

        val updateIntent =
            Intent(ACTION_UPDATE_WEATHER).apply {
                putExtra(LATITUDE, latitude)
                putExtra(LONGITUDE, longitude)
            }
        LocalBroadcastManager.getInstance(context).sendBroadcast(updateIntent)
        val weatherData = getWeatherData()
        sendNotification(context, weatherData)
    }

    private fun getWeatherData(): String {
        return SharedPrefManager.run {
            val cityName = getString(KEY_CITY_NAME, DEFAULT_VALUE)
            val countryName = getString(KEY_COUNTRY_NAME, DEFAULT_VALUE)
            val description = getString(KEY_DESCRIPTION, DEFAULT_VALUE)
            val temperature = getFloat(KEY_TEMPERATURE, DEFAULT_TEMPERATURE)
            "Thời tiết tại $cityName, $countryName: $description, $temperature°C"
        }
    }

    private fun sendNotification(
        context: Context,
        weatherData: String,
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val openAppIntent = Intent(context, DetailFragment::class.java)
        val openAppPendingIntent =
            PendingIntent.getActivity(
                context,
                PENDING_INTENT_REQUEST_CODE,
                openAppIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )

        val viewDetailsIntent =
            Intent(context, MainActivity::class.java).apply {
                putExtra(KEY_FRAGMENT_TO_OPEN, FRAGMENT_DETAIL)
            }
        val viewDetailsPendingIntent =
            PendingIntent.getActivity(
                context,
                VIEW_DETAILS_REQUEST_CODE,
                viewDetailsIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )

        val notification =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText(weatherData)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(openAppPendingIntent)
                .addAction(R.drawable.ic_favourite, ACTION_VIEW_DETAILS, viewDetailsPendingIntent)
                .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val DEFAULT_VALUE = "Không xác định"
        const val DEFAULT_TEMPERATURE = 0.0f
        const val KEY_CITY_NAME = "cityName"
        const val KEY_COUNTRY_NAME = "countryName"
        const val KEY_DESCRIPTION = "description"
        const val KEY_TEMPERATURE = "temperature"
        const val KEY_FRAGMENT_TO_OPEN = "fragment_to_open"
        const val FRAGMENT_DETAIL = "DetailFragment"
        const val CHANNEL_ID = "weather_channel_id"
        const val CHANNEL_NAME = "Weather Notifications"
        const val NOTIFICATION_TITLE = "Thời tiết hôm nay"
        const val ACTION_VIEW_DETAILS = "Xem chi tiết"
        const val PENDING_INTENT_REQUEST_CODE = 0
        const val VIEW_DETAILS_REQUEST_CODE = 1
        const val NOTIFICATION_ID = 1
        const val DEFAULT_LATITUDE = 0.0f
        const val DEFAULT_LONGITUDE = 0.0f
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val ACTION_UPDATE_WEATHER = "ACTION_UPDATE_WEATHER"
    }
}
