package com.example.sun.data.repository.source.remote

import com.example.sun.data.model.CurrentWeather

interface OnResultListener<T> {
    fun onSuccess(data: CurrentWeather)

    fun onError(exception: Exception?)
}
