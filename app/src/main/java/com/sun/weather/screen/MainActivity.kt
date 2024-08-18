package com.sun.weather.screen

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.sun.weather.R
import com.sun.weather.databinding.ActivityMainBinding
import com.sun.weather.screen.favourite.FavouriteFragment
import com.sun.weather.screen.home.HomeFragment
import com.sun.weather.screen.notification.WeatherNotificationReceiver
import com.sun.weather.screen.notification.WeatherNotificationReceiver.Companion.DEFAULT_LATITUDE
import com.sun.weather.screen.notification.WeatherNotificationReceiver.Companion.DEFAULT_LONGITUDE
import com.sun.weather.utils.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDailyAlarm()
        LocalBroadcastManager.getInstance(this).registerReceiver(weatherReceiver, IntentFilter(ACTION_UPDATE_WEATHER))
    }

    private val weatherReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(
                context: Context,
                intent: Intent,
            ) {
                val latitude = intent.getFloatExtra(LATITUDE, DEFAULT_LATITUDE)
                val longitude = intent.getFloatExtra(LONGITUDE, DEFAULT_LONGITUDE)
                HomeFragment.homePresenter?.getCurrentLocationWeather(latitude.toDouble(), longitude.toDouble())
            }
        }

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initView() {
        setNextFragment(HomeFragment.newInstance())
        setNavigation()
    }

    private fun setNavigation() {
        viewBinding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.mi_home -> setNextFragment(HomeFragment.newInstance())
                R.id.mi_favorite -> setNextFragment(FavouriteFragment.newInstance())
            }
            true
        }
    }

    private fun setNextFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(fragment::javaClass.name)
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun setDailyAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, WeatherNotificationReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(
                this,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        val timeZone = TimeZone.getDefault()
        val calendar =
            Calendar.getInstance(timeZone).apply {
                set(Calendar.HOUR_OF_DAY, 6)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }
        if (calendar.before(Calendar.getInstance(timeZone))) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent,
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(weatherReceiver)
    }

    companion object {
        const val REQUEST_CODE = 1000
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val ACTION_UPDATE_WEATHER = "ACTION_UPDATE_WEATHER"
    }
}
