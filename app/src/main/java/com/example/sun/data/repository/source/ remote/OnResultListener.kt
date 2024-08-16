package com.example.sun.data.repository.source.remote

interface OnResultListener<T> {
    fun onSuccess(data: T)

    fun onError(exception: Exception?)
}
