package com.example.sun.screen.search

import android.view.LayoutInflater
import com.example.sun.utils.base.BaseFragment
import com.example.weather.databinding.FragmentSearchBinding

class SearchFragment : BaseFragment<FragmentSearchBinding>() {
    override fun inflateViewBinding(inflater: LayoutInflater): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater)
    }

    override fun initData() {
    }

    override fun initView() {
    }
    companion object {
        fun newInstance() = SearchFragment()
    }
}
