package com.example.androidhwsemester2.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.MutableCreationExtras
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.androidhwsemester2.R
import com.example.androidhwsemester2.databinding.FragmentCityInfoBinding
import com.example.androidhwsemester2.di.DataDependency
import com.example.androidhwsemester2.di.ViewModelArgsKey
import com.example.androidhwsemester2.presentation.viewmodel.ViewPagerViewModel

class CityInfoFragment : Fragment(R.layout.fragment_city_info) {

    private var dataDependency: DataDependency? = null
    private val viewBinding: FragmentCityInfoBinding by viewBinding(FragmentCityInfoBinding::bind)
    private val viewModel: ViewPagerViewModel by activityViewModels(extrasProducer = {
        MutableCreationExtras().apply {
            set(ViewModelArgsKey.getCitiesFromDataBaseUseCase, dataDependency?.getCitiesFromDataBaseUseCase!!)
            set(ViewModelArgsKey.getWeatherByNameUseCase, dataDependency?.getWeatherByNameUseCase!!)
            set(ViewModelArgsKey.saveCityUseCase, dataDependency?.saveCityInfoUseCase!!)
        }
    }) {
        ViewPagerViewModel.Factory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dataDependency = DataDependency(requireContext())
        observeData()
        initViews()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViews() {
        with(viewBinding){
            refreshLayout.setOnRefreshListener {
                viewModel.refreshData(arguments?.getInt(NUMBER_KEY) ?: -1)
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
            viewModel.isRefreshingState.observe(viewLifecycleOwner){flag ->
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
    }
}