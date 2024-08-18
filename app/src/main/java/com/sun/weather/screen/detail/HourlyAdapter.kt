package com.sun.weather.screen.detail

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sun.weather.data.model.HourlyForecastItem
import com.sun.weather.databinding.ForecastHourlyBinding
import java.util.Locale

class HourlyAdapter(private var forecastList: List<HourlyForecastItem>) : RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ForecastHourlyBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bindData(forecastList[position])
    }

    inner class ViewHolder(private val binding: ForecastHourlyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(item: HourlyForecastItem) {
            val dateTimeString = item.dtTxt
            val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val date = originalFormat.parse(dateTimeString)
            val newFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val timeString = newFormat.format(date!!)
            binding.forecastHourTv.text = timeString
            binding.forecastTemp.text = item.main.currentTemperature.toString()
            Glide.with(itemView.context.applicationContext)
                .load(item.iconWeather)
                .into(binding.statusImg)
        }
    }

    fun updateData(list: List<HourlyForecastItem>) {
        forecastList = list
        notifyDataSetChanged()
    }
}
