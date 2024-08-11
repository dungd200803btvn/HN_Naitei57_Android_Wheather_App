package com.example.sun.screen.favourite

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.sun.data.model.FavouriteLocation
import com.example.sun.screen.home.HomeFragment.Companion.getFavouriteLocations
import com.example.weather.databinding.ItemFavouriteBinding
import com.google.gson.Gson

class FavouriteAdapter(
    private val locations: MutableList<FavouriteLocation>,
) : RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemFavouriteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding = ItemFavouriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val location = locations[position]
        holder.binding.tvCityName.text = location.cityName
        holder.binding.tvCountryName.text = location.countryName
        holder.binding.btnHeart.setOnClickListener {
            locations.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
            removeFavouriteLocation(holder.itemView.context, location)
            Toast.makeText(holder.itemView.context, "Removed from favourites", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = locations.size
}

private fun removeFavouriteLocation(
    context: Context,
    location: FavouriteLocation,
) {
    val sharedPref = context.getSharedPreferences("favourite_locations", Context.MODE_PRIVATE)
    val editor = sharedPref.edit()

    val locations = getFavouriteLocations(context).toMutableList()
    locations.remove(location)

    editor.putString("favourite_locations", Gson().toJson(locations))
    editor.apply()
}
