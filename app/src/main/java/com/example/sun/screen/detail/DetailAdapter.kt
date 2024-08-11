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
import com.example.sun.utils.OnItemRecyclerViewClickListener
import com.example.weather.R

class DetailAdapter(private var listForecastDay: MutableList<List<DetailWeatherData>>) :
    RecyclerView.Adapter<DetailAdapter.ViewHolder>() {
    private var onItemClickListener: OnItemRecyclerViewClickListener<DetailWeatherData>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.forecast_day, parent, false)
        return ViewHolder(view, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return listForecastDay.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bindViewData(listForecastDay[position])
    }

    fun registerItemRecyclerViewClickListener(onItemRecyclerViewClickListener: OnItemRecyclerViewClickListener<DetailWeatherData>?) {
        onItemClickListener = onItemRecyclerViewClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(forecastDays: List<List<DetailWeatherData>>) {
        forecastDays?.let {
            this.listForecastDay = it.toMutableList()
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(
        itemView: View,
        itemClickListener: OnItemRecyclerViewClickListener<DetailWeatherData>?,
    ) : RecyclerView.ViewHolder(itemView) {
        private var mTextViewDay: TextView? = null
        private var mTextViewStatus: TextView? = null
        private var mTextViewMaxTemp: TextView? = null
        private var mTextViewMinTemp: TextView? = null
        private var mImageViewWeatherStatus: ImageView

        init {
            mTextViewDay = itemView.findViewById(R.id.tv_day)
            mTextViewStatus = itemView.findViewById(R.id.tv_status)
            mTextViewMaxTemp = itemView.findViewById(R.id.tv_max_temp)
            mTextViewMinTemp = itemView.findViewById(R.id.tv_min_temp)
            mImageViewWeatherStatus = itemView.findViewById(R.id.img_status)
            itemView.setOnClickListener {
                itemClickListener?.onItemClick(listForecastDay[adapterPosition].first())
            }
        }

        fun bindViewData(forecastDay: List<DetailWeatherData>) {
            forecastDay.let {
                mTextViewMaxTemp?.text = it.maxByOrNull { data -> data.maxTemp.toFloat() }?.maxTemp
                mTextViewMinTemp?.text = it.minByOrNull { data -> data.minTemp.toFloat() }?.minTemp
                mTextViewDay?.text = it.maxByOrNull { data -> data.maxTemp.toFloat() }?.day
                mTextViewStatus?.text = it.maxByOrNull { data -> data.maxTemp.toFloat() }?.status
                Glide.with(itemView.context.applicationContext)
                    .load(it.maxByOrNull { data -> data.maxTemp.toFloat() }?.iconWeather)
                    .into(mImageViewWeatherStatus)
            }
        }
    }
}
