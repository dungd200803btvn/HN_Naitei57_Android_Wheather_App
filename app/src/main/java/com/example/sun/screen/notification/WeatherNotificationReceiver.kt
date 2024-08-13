package com.example.sun.screen.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.sun.screen.MainActivity
import com.example.sun.screen.detail.DetailFragment
import com.example.weather.R

class WeatherNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        val weatherData = getWeatherData(context)
        sendNotification(context, weatherData)
    }

    private fun getWeatherData(context: Context): String {
        val sharedPref = context.getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
        val cityName = sharedPref.getString("cityName", "Không xác định")
        val countryName = sharedPref.getString("countryName", "Không xác định")
        val description = sharedPref.getString("description", "Không xác định")
        val temperature = sharedPref.getFloat("temperature", 0.0f)
        return "Thời tiết tại $cityName, $countryName: $description, $temperature°C"
    }

    private fun sendNotification(
        context: Context,
        weatherData: String,
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "weather_channel_id"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Weather Notifications", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val openAppIntent = Intent(context, DetailFragment::class.java)
        val openAppPendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                openAppIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )

        // Intent to view details (open DetailFragment)
        val viewDetailsIntent =
            Intent(context, MainActivity::class.java).apply {
                putExtra("fragment_to_open", "DetailFragment")
            }
        val viewDetailsPendingIntent =
            PendingIntent.getActivity(
                context,
                1,
                viewDetailsIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )

        val notification =
            NotificationCompat.Builder(context, channelId)
                .setContentTitle("Thời tiết hôm nay")
                .setContentText(weatherData)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(openAppPendingIntent) // Open the app on notification click
                .addAction(R.drawable.ic_favourite, "Xem chi tiết", viewDetailsPendingIntent) // Action 1: View details
                .build()

        notificationManager.notify(1, notification)
    }
}
