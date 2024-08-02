package com.example.sun.data.repository.source.remote.fetchjson
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.sun.data.model.CurrentWeather
import com.example.sun.data.repository.source.remote.OnResultListener
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ApiManager private constructor(private val threadPoolSize: Int) {
    private lateinit var mExecutor: ExecutorService
    private val mHandler = Handler(Looper.getMainLooper())
    private val logTag = "ApiManager"
    private val timeOut = 15000
    private val methodGet = "GET"

    private fun getExecutor(): ExecutorService {
        if (!::mExecutor.isInitialized) {
            mExecutor = Executors.newFixedThreadPool(threadPoolSize)
        }
        return mExecutor
    }

    fun executeApiCall(
        urlString: String,
        listener: OnResultListener<CurrentWeather>,
    ) {
        getExecutor().submit {
            try {
                val responseJson = getJsonStringFromUrl(urlString)
                val gson = Gson()
                val weather = gson.fromJson(responseJson, CurrentWeather::class.java)
                mHandler.post {
                    listener.onSuccess(weather)
                }
            } catch (e: Exception) {
                mHandler.post {
                    listener.onError(e)
                }
            }
        }
    }

    private fun getJsonStringFromUrl(urlString: String): String {
        val url = URL(urlString)
        val httpURLConnection = url.openConnection() as? HttpURLConnection
        return try {
            httpURLConnection?.run {
                connectTimeout = timeOut
                readTimeout = timeOut
                requestMethod = methodGet
                doOutput = true
                connect()
            }
            if (httpURLConnection?.responseCode == HttpURLConnection.HTTP_OK) {
                val bufferedReader = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
                val stringBuilder = StringBuilder()
                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }
                bufferedReader.close()
                stringBuilder.toString()
            } else {
                Log.d(logTag, "HTTP error code: ${httpURLConnection?.responseCode}")
                throw Exception("City not found: $urlString")
            }
        } catch (e: MalformedURLException) {
            Log.d(logTag, "MalformedURLException: ${e.message}")
            throw e
        } catch (e: IOException) {
            Log.d(logTag, "IOException: ${e.message}")
            throw e
        } catch (e: Exception) {
            Log.d(logTag, "Exception: ${e.message}")
            throw e
        } finally {
            httpURLConnection?.disconnect()
        }
    }

    companion object {
        @Volatile
        private var instance: ApiManager? = null

        fun newInstance(threadPoolSize: Int): ApiManager {
            return instance ?: synchronized(this) {
                instance ?: ApiManager(threadPoolSize).also { instance = it }
            }
        }
    }
}
