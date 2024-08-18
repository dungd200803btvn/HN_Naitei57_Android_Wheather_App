package com.example.sun.screen.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sun.data.model.FavouriteLocation
import com.example.sun.utils.listener.OnItemClickListener
import com.example.weather.databinding.ItemFavouriteBinding

class FavouriteAdapter(private var listener: OnItemClickListener) : RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {
    private val mListWeathers by lazy { mutableListOf<FavouriteLocation>() }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemFavouriteBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return mListWeathers.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bindData(mListWeathers[position])
        holder.buttonFavourite.setOnClickListener {
            listener.onItemClickListener(holder.itemView, position)
        }
    }

    fun updateData(listData: List<FavouriteLocation>) {
        mListWeathers.clear()
        if (listData != null) {
            mListWeathers.addAll(listData)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemFavouriteBinding) : RecyclerView.ViewHolder(binding.root) {
        val buttonFavourite = binding.btnHeart

        fun bindData(newItem: FavouriteLocation) {
            binding.tvCityName.text = newItem.cityName
            binding.tvCountryName.text = newItem.countryName
        }
    }
}
