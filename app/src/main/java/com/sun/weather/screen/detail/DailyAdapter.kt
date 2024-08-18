package com.sun.weather.screen.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sun.weather.data.model.WeeklyForecastItem
import com.sun.weather.databinding.ForecastDailyBinding

class DailyAdapter(private var forecastList: List<WeeklyForecastItem>) : RecyclerView.Adapter<DailyAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ForecastDailyBinding.inflate(inflater, parent, false))
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

    inner class ViewHolder(private val binding: ForecastDailyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(item: WeeklyForecastItem) {
            binding.tvDay.text = item.day
            binding.tvStatus.text = item.weather[0].description
            binding.tvMaxTemp.text = item.temp.max.toString()
            binding.tvMinTemp.text = item.temp.min.toString()
            Glide.with(itemView.context.applicationContext)
                .load(item.iconWeather)
                .into(binding.imgStatus)
        }
    }

    fun updateData(list: List<WeeklyForecastItem>) {
        forecastList = list
        notifyDataSetChanged()
    }
}
