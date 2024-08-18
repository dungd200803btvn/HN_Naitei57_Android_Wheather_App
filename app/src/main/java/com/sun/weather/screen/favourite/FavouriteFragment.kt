package com.sun.weather.screen.favourite

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.sun.weather.R
import com.sun.weather.data.model.FavouriteLocation
import com.sun.weather.data.repository.source.WeatherRepository
import com.sun.weather.data.repository.source.local.LocalDataSourceImpl
import com.sun.weather.data.repository.source.remote.RemoteDataSourceImpl
import com.sun.weather.databinding.FragmentFavouriteBinding
import com.sun.weather.utils.base.BaseFragment
import com.sun.weather.utils.ext.goBackFragment
import com.sun.weather.utils.listener.OnItemClickListener
import java.io.IOException

class FavouriteFragment :
    BaseFragment<FragmentFavouriteBinding>(),
    FavouriteContract.View,
    OnItemClickListener {
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
