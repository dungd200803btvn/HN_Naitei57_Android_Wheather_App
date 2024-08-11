package com.example.sun.screen.detail

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DetailFragment :
    BaseFragment<FragmentDetailBinding>(),
    DetailContract.View,
    OnItemRecyclerViewClickListener<DetailWeatherData> {
    private var detailPresenter: DetailPresenter? = null
    private var cityName: String = ""
    private val myTag = "DetailFragment"
    private lateinit var detailAdapter: DetailAdapter
    private lateinit var hourlyAdapter: HourlyAdapter

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
        // RecyclerView horizontal
        hourlyAdapter = HourlyAdapter(mutableListOf())
        viewBinding.recyclerViewHourly.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = hourlyAdapter
        }

        // RecyclerView vertical
        detailAdapter = DetailAdapter(mutableListOf())
        viewBinding.listView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = detailAdapter
            detailAdapter.registerItemRecyclerViewClickListener(this@DetailFragment)
        }
    }

    private fun groupDataByDay(forecastList: List<Forecast1Day>): List<List<DetailWeatherData>> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return forecastList.groupBy { forecast ->
            val date = dateFormat.parse(forecast.dtTxt)
            dateFormat.format(date)
        }.values.map { dayForecast ->
            dayForecast.map { toData(it) }
        }
    }

    private fun toData(forecastDay: Forecast1Day): DetailWeatherData {
        return DetailWeatherData(
            day = forecastDay.dtTxt.split(" ")[0],
            status = forecastDay.weather[0].description,
            maxTemp = forecastDay.main.tempMax.toString(),
            minTemp = forecastDay.main.tempMin.toString(),
            iconWeather = Constant.BASE_ICON_URL + forecastDay.weather[0].icon + "@2x.png",
            time = forecastDay.dtTxt.split(" ")[1],
            fulltime = forecastDay.dtTxt,
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onGetForecastDaySuccess(listForecastDay: ForecastDay) {
        // Lấy ngày và giờ hiện tại từ hệ thống
        val currentDate = LocalDate.now()
        val currentTime = LocalTime.now()

        cityName = "Forecast Detail of City: " + listForecastDay.city.cityName + "," + listForecastDay.city.countryName
        viewBinding.tvToolbarTitle.text = cityName

        // Lọc và nhóm các dự báo theo ngày, bỏ qua các ngày trước ngày hiện tại
        val groupedByDay =
            listForecastDay.forecastList
                .filter { forecast ->
                    val forecastDate = LocalDate.parse(forecast.dtTxt.split(" ")[0])
                    !forecastDate.isBefore(currentDate)
                }
                .chunked(8)
                .map { dayForecast ->
                    dayForecast.map { toData(it) }
                }

        // Lọc và lấy các dự báo giờ, bỏ qua các giờ trước giờ hiện tại nếu nó là ngày hiện tại
        val hourlyData =
            groupedByDay.first().take(7).filter { hourlyForecast ->
                val forecastDateTime = LocalDateTime.parse(hourlyForecast.fulltime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                !forecastDateTime.toLocalDate().isEqual(currentDate) || !forecastDateTime.toLocalTime().isBefore(currentTime)
            }

        hourlyAdapter.setData(hourlyData.toMutableList())

        // Nhóm dữ liệu theo ngày và set dữ liệu cho detailAdapter
        val groupedData =
            groupDataByDay(
                listForecastDay.forecastList
                    .filter { forecast ->
                        val forecastDate = LocalDate.parse(forecast.dtTxt.split(" ")[0])
                        !forecastDate.isBefore(currentDate)
                    },
            )
        detailAdapter.setData(groupedData)
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
