package com.example.sun.screen.home

import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.example.sun.data.model.CurrentWeather
import com.example.sun.data.repository.source.CurrentWeatherRepository
import com.example.sun.data.repository.source.local.LocalDataSourceImpl
import com.example.sun.data.repository.source.remote.RemoteDataSourceImpl
import com.example.sun.utils.base.BaseFragment
import com.example.weather.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(), HomeContract.View {
    private var homePresenter: HomePresenter? = null
    private val myTag = "HomeFragment"

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater)
    }

    override fun initData() {
        homePresenter =
            HomePresenter(
                CurrentWeatherRepository.getInstance(
                    RemoteDataSourceImpl.getInstance(),
                    LocalDataSourceImpl.getInstance(),
                ),
            )
        homePresenter?.setView(this)
    }

    override fun initView() {
    }

    override fun onGetCurrentWeatherSuccess(currentWeather: CurrentWeather) {
    }

    override fun onError(e: String) {
        Log.d(MY_TAG, "onError: $e")
        Toast.makeText(context, "Lỗi: $e", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        homePresenter = null
    }

    companion object {
        fun newInstance() = HomeFragment()

        const val MY_TAG = "HomeFragment"
    }
}
