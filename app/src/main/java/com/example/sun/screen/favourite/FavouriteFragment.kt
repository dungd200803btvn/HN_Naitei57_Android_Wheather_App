package com.example.sun.screen.favourite

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sun.data.model.FavouriteLocation
import com.example.sun.data.repository.source.WeatherRepository
import com.example.sun.data.repository.source.local.LocalDataSourceImpl
import com.example.sun.data.repository.source.remote.RemoteDataSourceImpl
import com.example.sun.utils.base.BaseFragment
import com.example.sun.utils.ext.goBackFragment
import com.example.sun.utils.listener.OnItemClickListener
import com.example.weather.R
import com.example.weather.databinding.FragmentFavouriteBinding
import java.io.IOException

class FavouriteFragment : BaseFragment<FragmentFavouriteBinding>(), FavouriteContract.View, OnItemClickListener {
    private var favouritePresenter: FavouritePresenter? = null
    private lateinit var favoriteAdapter: FavouriteAdapter
    private var listFavourite: List<FavouriteLocation>? = null

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentFavouriteBinding {
        return FragmentFavouriteBinding.inflate(inflater)
    }

    override fun initData() {
        favouritePresenter =
            FavouritePresenter(
                WeatherRepository.getInstance(
                    RemoteDataSourceImpl.getInstance(),
                    LocalDataSourceImpl.getInstance(requireContext()),
                ),
            )
        favouritePresenter?.setView(this)
        favouritePresenter?.getAllFavorite()
    }

    override fun initView() {
        favoriteAdapter = FavouriteAdapter(this)
        viewBinding.rvFavorite.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoriteAdapter
        }
        viewBinding.toolBar.findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            goBackFragment()
        }
    }

    override fun onGetFavoriteListSuccess(favoriteList: List<FavouriteLocation>) {
        listFavourite = favoriteList
        favoriteAdapter.updateData(favoriteList)
    }

    override fun onItemClickListener(
        view: View,
        position: Int,
    ) {
        val favouriteLocation = listFavourite?.get(position)
        try {
            if (favouriteLocation != null) {
                favouriteLocation.id?.let { favouritePresenter?.removeFavoriteItem(it) }
            }
        } catch (e: IOException) {
            println(e)
        } finally {
            favouritePresenter?.getAllFavorite()
        }
    }
    companion object {
        fun newInstance() = FavouriteFragment()
    }
}
