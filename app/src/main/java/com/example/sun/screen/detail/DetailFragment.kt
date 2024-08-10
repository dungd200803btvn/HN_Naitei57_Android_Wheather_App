package com.example.sun.screen.detail

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sun.data.model.DetailWeatherData
import com.example.sun.data.model.Forecast1Day
import com.example.sun.data.model.ForecastDay
import com.example.sun.data.repository.WeatherRepository
import com.example.sun.data.repository.source.local.LocalDataSourceImpl
import com.example.sun.data.repository.source.remote.RemoteDataSourceImpl
import com.example.sun.utils.OnItemRecyclerViewClickListener
import com.example.sun.utils.base.BaseFragment
import com.example.sun.utils.base.Constant
import com.example.weather.R
import com.example.weather.databinding.FragmentDetailBinding

class DetailFragment :
    BaseFragment<FragmentDetailBinding>(),
    DetailContract.View,
    OnItemRecyclerViewClickListener<DetailWeatherData> {
    private var detailPresenter: DetailPresenter? = null
    private var cityName: String = ""
    private val myTag = "DetailFragment"
    private lateinit var detailAdapter: DetailAdapter

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentDetailBinding {
        return FragmentDetailBinding.inflate(inflater)
    }

    override fun initData() {
        detailPresenter =
            DetailPresenter(
                WeatherRepository.getInstance(
                    RemoteDataSourceImpl.getInstance(),
                    LocalDataSourceImpl.getInstance(),
                ),
            )
        detailPresenter?.setView(this)
        val sharedPref = requireContext().getSharedPreferences("home_fragment_prefs", Context.MODE_PRIVATE)
        val latitude = sharedPref.getFloat("latitude", 0.0f).toDouble()
        val longitude = sharedPref.getFloat("longitude", 0.0f).toDouble()
        detailPresenter?.getForecastDay(latitude, longitude)
        viewBinding.toolbar.findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun initView() {
        detailAdapter = DetailAdapter(mutableListOf())
        viewBinding.listView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = detailAdapter
            detailAdapter.registerItemRecyclerViewClickListener(this@DetailFragment)
        }
        val dividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.custom_divider)!!)
        viewBinding.listView.addItemDecoration(dividerItemDecoration)
    }

    private fun toData(forecastDay: Forecast1Day): DetailWeatherData {
        return DetailWeatherData(
            day = forecastDay.dtTxt,
            status = forecastDay.weather[0].description,
            maxTemp = forecastDay.main.tempMax.toString(),
            minTemp = forecastDay.main.tempMin.toString(),
            iconWeather = Constant.BASE_ICON_URL + forecastDay.weather[0].icon + "@2x.png",
        )
    }

    override fun onGetForecastDaySuccess(listForecastDay: ForecastDay) {
        Log.v("LCD", "onGetForecastDaySuccess:" + listForecastDay.city.cityName + "," + listForecastDay.city.countryName)
        cityName = "Forecast Detail of City: " + listForecastDay.city.cityName + "," + listForecastDay.city.countryName
        viewBinding.tvToolbarTitle.text = cityName
        val detailWeatherDataList: List<DetailWeatherData> =
            listForecastDay.forecastList.map { forecastDay ->
                toData(forecastDay)
            }
        detailAdapter.setData(detailWeatherDataList.toMutableList())
    }

    override fun onError(e: String) {
        Log.v(myTag, "onError: $e")
    }

    override fun onItemClick(item: DetailWeatherData?) {
        Toast.makeText(requireContext(), "Click on ${item?.day}", Toast.LENGTH_SHORT).show()
    }
    companion object {
        fun newInstance() = DetailFragment()
    }
}