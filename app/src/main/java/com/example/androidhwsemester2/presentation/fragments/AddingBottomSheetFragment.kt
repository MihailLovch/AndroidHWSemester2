package com.example.androidhwsemester2.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.MutableCreationExtras
import com.example.androidhwsemester2.R
import com.example.androidhwsemester2.databinding.DialogFragmentAddingBinding
import com.example.androidhwsemester2.di.DataDependency
import com.example.androidhwsemester2.di.ViewModelArgsKey
import com.example.androidhwsemester2.presentation.viewmodel.ViewPagerViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddingBottomSheetFragment : BottomSheetDialogFragment(R.layout.dialog_fragment_adding) {
    private var dataDependency: DataDependency? = null
    private var _viewBinding: DialogFragmentAddingBinding? = null
    private val viewBinding: DialogFragmentAddingBinding get() = _viewBinding!!

    private val viewModel: ViewPagerViewModel by activityViewModels(extrasProducer = {
        MutableCreationExtras().apply {
            set(ViewModelArgsKey.getCitiesFromDataBaseUseCase, dataDependency?.getCitiesFromDataBaseUseCase!!)
            set(ViewModelArgsKey.getWeatherByNameUseCase, dataDependency?.getWeatherByNameUseCase!!)
            set(ViewModelArgsKey.saveCityUseCase, dataDependency?.saveCityInfoUseCase!!)
        }
    }) {
        ViewPagerViewModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = DialogFragmentAddingBinding.inflate(inflater)
        dataDependency = DataDependency(requireContext())
        initViews()
        return viewBinding.root
    }

    private fun initViews() {
        with(viewBinding){
            addingBtn.setOnClickListener {
                viewModel.addNewCityToList(nameEt.editText?.text.toString())
                dismiss()
            }
        }
    }

    companion object {
        fun newInstance(): AddingBottomSheetFragment = AddingBottomSheetFragment()
        const val TAG = "AddingBottomSheetFragmentTAG"
    }
}