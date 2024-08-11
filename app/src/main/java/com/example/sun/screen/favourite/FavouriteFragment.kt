package com.example.sun.screen.favourite

import android.view.LayoutInflater
import android.widget.ImageButton
import com.example.sun.screen.home.HomeFragment.Companion.getFavouriteLocations
import com.example.sun.utils.base.BaseFragment
import com.example.weather.R
import com.example.weather.databinding.FragmentFavouriteBinding

class FavouriteFragment : BaseFragment<FragmentFavouriteBinding>() {
    private lateinit var favouriteAdapter: FavouriteAdapter

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentFavouriteBinding {
        return FragmentFavouriteBinding.inflate(inflater)
    }

    override fun initData() {
        viewBinding.toolbar.findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun initView() {
        val favouriteLocations = getFavouriteLocations(requireContext()).toMutableList()
        favouriteAdapter = FavouriteAdapter(favouriteLocations)
        viewBinding.rvFavorite.adapter = favouriteAdapter
    }
    companion object {
        fun newInstance() = FavouriteFragment()
    }
}
