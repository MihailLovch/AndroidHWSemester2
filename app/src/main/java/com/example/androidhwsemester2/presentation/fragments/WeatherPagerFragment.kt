package com.example.androidhwsemester2.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.MutableCreationExtras
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androidhwsemester2.R
import com.example.androidhwsemester2.databinding.FragmentViewPagerBinding
import com.example.androidhwsemester2.di.DataDependency
import com.example.androidhwsemester2.di.ViewModelArgsKey
import com.example.androidhwsemester2.presentation.CityPagerAdapter
import com.example.androidhwsemester2.presentation.extensions.shortToast
import com.example.androidhwsemester2.presentation.viewmodel.ViewPagerViewModel

class WeatherPagerFragment : Fragment(R.layout.fragment_view_pager) {

    private var dataDependency: DataDependency? = null
    private var adapter: CityPagerAdapter? = null
    private val viewBinding: FragmentViewPagerBinding by viewBinding(FragmentViewPagerBinding::bind)
    private val viewModel: ViewPagerViewModel by activityViewModels(extrasProducer = {
        MutableCreationExtras().apply {
            set(
                ViewModelArgsKey.getCitiesFromDataBaseUseCase,
                dataDependency?.getCitiesFromDataBaseUseCase!!
            )
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

    private fun observeData() {
        with(viewBinding) {
            viewModel.errorState.observe(viewLifecycleOwner) { message ->
                if (message.isNotBlank()) {
                    shortToast(message)
                    AddingBottomSheetFragment.newInstance()
                        .show(childFragmentManager, AddingBottomSheetFragment.TAG)
                }
            }
            viewModel.listState.observe(viewLifecycleOwner) { dataSize ->
                dataSize?.let {
                    adapter?.submitList(viewModel.getCurrentCities())
                }
            }
            viewModel.firstDialogState.observe(viewLifecycleOwner) { flag ->
                if (flag) {
                    AddingBottomSheetFragment.newInstance()
                        .show(childFragmentManager, AddingBottomSheetFragment.TAG)
                }
            }
            viewModel.elementAddedState.observe(viewLifecycleOwner) { dataSize ->
                dataSize?.let {
                    cityInfoVp.currentItem = dataSize
                }
            }
        }
    }

    private fun initViews() {
        with(viewBinding) {
            addBtn.setOnClickListener {
                AddingBottomSheetFragment.newInstance()
                    .show(childFragmentManager, AddingBottomSheetFragment.TAG)
            }
            adapter = CityPagerAdapter(requireActivity(), viewModel.getCurrentCities())
            cityInfoVp.adapter = adapter

        }
    }

    companion object {
        fun newInstance(): WeatherPagerFragment = WeatherPagerFragment()
        const val TAG = "WeatherPagerFragmentTAG"
    }
}