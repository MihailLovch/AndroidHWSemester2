package com.example.androidhwsemester2.presentation.adapters

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.androidhwsemester2.presentation.fragments.CityInfoFragment
import com.example.androidhwsemester2.presentation.model.WeatherDataModel

class CityPagerAdapter(
    activity: FragmentActivity,
    private var list: List<WeatherDataModel?>,
) : FragmentStateAdapter(activity) {

    fun submitList(list: List<WeatherDataModel?>){
        this.list = list
        notifyDataSetChanged() // :(
    }

    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment {
        return CityInfoFragment.newInstance(bundleOf(CityInfoFragment.NUMBER_KEY to position))
    }
}