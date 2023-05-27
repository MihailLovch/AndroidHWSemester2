package com.example.androidhwsemester2.presentation.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.example.androidhwsemester2.R
import com.example.androidhwsemester2.databinding.DialogFragmentDailyInfoBinding
import com.example.androidhwsemester2.di.dagger.appComponent
import com.example.androidhwsemester2.di.dagger.lazyViewModel
import com.example.androidhwsemester2.presentation.adapters.DailyInfoAdapter
import com.example.androidhwsemester2.presentation.viewmodel.DailyInfoViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class DailyWeatherInfoFragment : BottomSheetDialogFragment(R.layout.dialog_fragment_daily_info) {

    private var _viewBinding: DialogFragmentDailyInfoBinding? = null
    private val viewBinding: DialogFragmentDailyInfoBinding get() = _viewBinding!!

    private val viewModel: DailyInfoViewModel by lazyViewModel {
        requireContext().appComponent().dailyInfoViewModel().create(
            lat = arguments?.getDouble(CityInfoFragment.LAT_KEY) ?: 0.0,
            long = arguments?.getDouble(CityInfoFragment.LONG_KEY) ?: 0.0
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
    }

    private fun initViews() {
        with(viewBinding) {
            infoRv.adapter = DailyInfoAdapter()
        }

        dialog?.setOnShowListener {
            val d = dialog as BottomSheetDialog
            val view: FrameLayout = d.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
            BottomSheetBehavior.from(view).state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun observeData() {
        with(viewBinding) {
            viewModel.listWeatherState.observe(viewLifecycleOwner) { list ->
                list?.let { newList ->
                    (infoRv.adapter as DailyInfoAdapter).submitList(newList)
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = DialogFragmentDailyInfoBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroy() {
        _viewBinding = null
        super.onDestroy()
    }

    companion object {
        fun newInstance(bundle: Bundle): DailyWeatherInfoFragment =
            DailyWeatherInfoFragment().apply { arguments = bundle }

        const val TAG = "DailyWeatherInfoFragment"
    }
}