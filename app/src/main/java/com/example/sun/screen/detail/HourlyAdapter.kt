package com.example.sun.screen.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sun.data.model.DetailWeatherData
import com.example.weather.R

class HourlyAdapter(private var listForecastHour: MutableList<DetailWeatherData>) :
    RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): HourlyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.forecast_hour, parent, false)
        return HourlyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listForecastHour.size
    }

    override fun onBindViewHolder(
        holder: HourlyViewHolder,
        position: Int,
    ) {
        holder.bindViewData(listForecastHour[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(forecastHours: MutableList<DetailWeatherData>) {
        listForecastHour = forecastHours
        notifyDataSetChanged()
    }

    inner class HourlyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var mTextViewHour: TextView? = null
        private var mTextViewTemp: TextView? = null
        private var mImageViewWeatherStatus: ImageView

        init {
            mTextViewHour = itemView.findViewById(R.id.forecast_hour)
            mTextViewTemp = itemView.findViewById(R.id.forecast_t)
            mImageViewWeatherStatus = itemView.findViewById(R.id.forecast_img)
        }

        fun bindViewData(forecastHour: DetailWeatherData) {
            mTextViewHour?.text = forecastHour.time
            mTextViewTemp?.text = forecastHour.maxTemp
            Glide.with(itemView.context)
                .load(forecastHour.iconWeather)
                .into(mImageViewWeatherStatus)
        }
    }
}
