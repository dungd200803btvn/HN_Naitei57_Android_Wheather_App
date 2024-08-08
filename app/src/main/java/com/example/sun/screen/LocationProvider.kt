package com.example.sun.screen

import android.location.Location

interface LocationProvider {
    fun retrieveCurrentLocation(): Location?
}
