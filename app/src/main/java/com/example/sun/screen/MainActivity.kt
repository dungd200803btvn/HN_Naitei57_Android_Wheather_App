package com.example.sun.screen

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.sun.screen.home.HomeFragment
import com.example.sun.utils.base.BaseActivity
import com.example.weather.R
import com.example.weather.databinding.ActivityMainBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task

class MainActivity : BaseActivity<ActivityMainBinding>(), LocationProvider {
    var currentLocation: Location? = null

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
            }
            true
        }
    }

    private fun setNextFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(fragment::javaClass.name)
            .replace(R.id.fl_container, fragment)
            .commit()
    }

    override fun retrieveCurrentLocation(): Location? {
        if (currentLocation == null) {
            requestLocation()
        }
        return currentLocation
    }

    private fun requestLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
                1000,
            )
        } else {
            getLastLocation()
            Log.v("myTag1", "Location: ${currentLocation?.latitude}, ${currentLocation?.longitude}")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation()
        } else {
            Toast.makeText(this, "Permissions are required to fetch the location", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1,
            )
            return
        }

        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val locationTask: Task<Location> = fusedLocationProviderClient.lastLocation
        locationTask.addOnSuccessListener(
            OnSuccessListener<Location?> { location ->
                if (location != null) {
                    currentLocation = location
                }
            },
        )
    }
}
