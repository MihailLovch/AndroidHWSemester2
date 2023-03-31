package com.example.androidhwsemester2.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.viewpager.widget.ViewPager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.androidhwsemester2.R
import com.example.androidhwsemester2.databinding.FragmentCityInfoBinding
import com.example.androidhwsemester2.di.dagger.appComponent
import com.example.androidhwsemester2.di.dagger.lazyViewModel
import com.example.androidhwsemester2.di.manual.DataDependency
import com.example.androidhwsemester2.di.manual.ViewModelArgsKey
import com.example.androidhwsemester2.presentation.viewmodel.ViewModelFactory
import com.example.androidhwsemester2.presentation.viewmodel.ViewPagerViewModel
import javax.inject.Inject

class CityInfoFragment : Fragment(R.layout.fragment_city_info) {

    private val viewBinding: FragmentCityInfoBinding by viewBinding(FragmentCityInfoBinding::bind)

    //    private val viewModel: ViewPagerViewModel by activityViewModels(extrasProducer = {
//        MutableCreationExtras().apply {
//            set(ViewModelArgsKey.getCitiesFromDataBaseUseCase, dataDependency?.getCitiesFromDataBaseUseCase!!)
//            set(ViewModelArgsKey.getWeatherByNameUseCase, dataDependency?.getWeatherByNameUseCase!!)
//            set(ViewModelArgsKey.saveCityUseCase, dataDependency?.saveCityInfoUseCase!!)
//        }
//    }) {
//        ViewPagerViewModel.Factory
//    }
    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: ViewPagerViewModel by activityViewModels{
        factory
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
    }
}