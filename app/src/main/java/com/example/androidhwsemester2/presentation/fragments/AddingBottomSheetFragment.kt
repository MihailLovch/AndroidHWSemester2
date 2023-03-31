package com.example.androidhwsemester2.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.MutableCreationExtras
import com.example.androidhwsemester2.R
import com.example.androidhwsemester2.databinding.DialogFragmentAddingBinding
import com.example.androidhwsemester2.di.dagger.appComponent
import com.example.androidhwsemester2.di.dagger.lazyViewModel
import com.example.androidhwsemester2.di.manual.DataDependency
import com.example.androidhwsemester2.di.manual.ViewModelArgsKey
import com.example.androidhwsemester2.presentation.viewmodel.ViewModelFactory
import com.example.androidhwsemester2.presentation.viewmodel.ViewPagerViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class AddingBottomSheetFragment : BottomSheetDialogFragment(R.layout.dialog_fragment_adding) {

    private var _viewBinding: DialogFragmentAddingBinding? = null
    private val viewBinding: DialogFragmentAddingBinding get() = _viewBinding!!

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: ViewPagerViewModel by activityViewModels{
        factory
    }
    override fun onAttach(context: Context) {
        requireContext().appComponent().inject(this)
        super.onAttach(context)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = DialogFragmentAddingBinding.inflate(inflater)
        Log.d("viewModel", viewModel.toString())
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