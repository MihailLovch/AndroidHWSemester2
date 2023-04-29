package com.example.androidhwsemester2.presentation.fragments.debug

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androidhwsemester2.R
import com.example.androidhwsemester2.databinding.FragmentDebugBinding
import com.example.androidhwsemester2.di.dagger.appComponent
import com.example.androidhwsemester2.di.dagger.lazyViewModel
import com.example.androidhwsemester2.presentation.MainActivity
import com.example.androidhwsemester2.presentation.adapters.DebugAdapter
import com.example.androidhwsemester2.presentation.fragments.WeatherPagerFragment


class DebugFragment : Fragment(R.layout.fragment_debug) {

    private val viewBinding: FragmentDebugBinding by viewBinding(FragmentDebugBinding::bind)

    private val viewModel: DebugViewModel by lazyViewModel {
        requireContext().appComponent().debugViewModel().create()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        initViews()
    }

    private fun initViews() {
        viewBinding.restartBtn.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            requireContext().startActivity(intent)
            if (context is Activity) {
                (context as Activity).finish()
            }

            Runtime.getRuntime().exit(0)
        }
    }

    override fun onDestroy() {
        (activity as MainActivity).registerShakingListener()
        super.onDestroy()
    }

    override fun onAttach(context: Context) {
        (activity as MainActivity).unregisterShakingListener()
        super.onAttach(context)
    }

    private fun observe() {
        with(viewBinding) {

            viewModel.lastTimeRequestState.observe(viewLifecycleOwner) { date ->
                lastRequestDateTv.text = context?.getString(R.string.last_date, date.toString())
            }
            viewModel.requestCountState.observe(viewLifecycleOwner) { list ->
                numbOfCitiesTv.text = context?.getString(R.string.number_of_cities, list.size)

                requestAmountRv.adapter = DebugAdapter(list)
            }
        }
    }

    companion object {
        fun newInstance() = DebugFragment()
        const val TAG = "DebugFragment"
    }
}