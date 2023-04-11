package com.example.androidhwsemester2.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.androidhwsemester2.R
import com.example.androidhwsemester2.databinding.FragmentCityInfoBinding
import com.example.androidhwsemester2.di.dagger.appComponent
import com.example.androidhwsemester2.di.dagger.lazyViewModel
import com.example.androidhwsemester2.presentation.viewmodel.ViewPagerViewModel

class CityInfoFragment : Fragment(R.layout.fragment_city_info) {

    private val viewBinding: FragmentCityInfoBinding by viewBinding(FragmentCityInfoBinding::bind)

    private val viewModel: ViewPagerViewModel by lazyViewModel(true){
        requireContext().appComponent().viewPagerViewModel().create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("viewModel", viewModel.toString())
        observeData()
        initViews()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        requireContext().appComponent().inject(this)
        super.onAttach(context)
    }
    private fun initViews() {
        with(viewBinding) {
            viewModel.refreshData()

            refreshLayout.setOnRefreshListener {
                viewModel.refreshData(arguments?.getInt(NUMBER_KEY) ?: -1)
            }

            cityNameTv.setOnClickListener {
                val model = viewModel.weatherListState[arguments?.getInt(NUMBER_KEY) ?: -1].value
                DailyWeatherInfoFragment.newInstance(bundleOf(
                    LAT_KEY to model?.lat,
                    LONG_KEY to model?.lon
                )).show(parentFragmentManager,DailyWeatherInfoFragment.TAG)
            }
        }
    }

    private fun observeData() {
        with(viewBinding) {
            viewModel
                .weatherListState[arguments?.getInt(NUMBER_KEY) ?: -1]
                .observe(viewLifecycleOwner) { model ->
                    model?.let {
                        Glide
                            .with(requireContext())
                            .load(getString(R.string.icon_uri, model.iconId))
                            .into(weatherIconIv)
                        cityNameTv.text = getString(R.string.city_name, model.cityName)
                        temperatureTv.text = getString(R.string.temperature, model.temperature)
                    }
                }
            viewModel.isRefreshingState.observe(viewLifecycleOwner) { flag ->
                flag?.let {
                    refreshLayout.isRefreshing = flag
                }
            }
        }
    }


    companion object {
        fun newInstance(bundle: Bundle): CityInfoFragment = CityInfoFragment().apply {
            arguments = bundle
        }

        const val NUMBER_KEY = "NUMBER_KEY"
        const val LAT_KEY = "LAT_KEY"
        const val LONG_KEY = "LONG_KEY"
    }
}