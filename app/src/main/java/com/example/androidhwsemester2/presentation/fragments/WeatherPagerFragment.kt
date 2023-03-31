package com.example.androidhwsemester2.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androidhwsemester2.R
import com.example.androidhwsemester2.WeatherApplication
import com.example.androidhwsemester2.databinding.FragmentViewPagerBinding
import com.example.androidhwsemester2.di.dagger.appComponent
import com.example.androidhwsemester2.di.dagger.lazyViewModel
import com.example.androidhwsemester2.presentation.CityPagerAdapter
import com.example.androidhwsemester2.presentation.extensions.shortToast
import com.example.androidhwsemester2.presentation.viewmodel.ViewModelFactory
import com.example.androidhwsemester2.presentation.viewmodel.ViewPagerViewModel
import com.example.androidhwsemester2.presentation.viewmodel.ViewPagerViewModel_Factory
import javax.inject.Inject

class WeatherPagerFragment : Fragment(R.layout.fragment_view_pager) {

    private var adapter: CityPagerAdapter? = null
    private val viewBinding: FragmentViewPagerBinding by viewBinding(FragmentViewPagerBinding::bind)

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: ViewPagerViewModel by activityViewModels{
        factory
    }

    override fun onAttach(context: Context) {
        (context.applicationContext as WeatherApplication).appComponent().inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("viewModel", viewModel.toString())
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