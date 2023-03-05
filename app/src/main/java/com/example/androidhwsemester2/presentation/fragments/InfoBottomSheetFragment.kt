package com.example.androidhwsemester2.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResultListener
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androidhwsemester2.R
import com.example.androidhwsemester2.data.model.WeatherResponse
import com.example.androidhwsemester2.databinding.DialogFragmentInfoBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InfoBottomSheetFragment : BottomSheetDialogFragment(R.layout.dialog_fragment_info){

    private var _viewBinding: DialogFragmentInfoBinding? = null
    private val viewBinding: DialogFragmentInfoBinding get() = _viewBinding!!
    private var weatherResponse: WeatherResponse? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = DialogFragmentInfoBinding.inflate(inflater)
        subscribeToChanges()
//        weatherResponse = arguments?.getSerializable(WeatherFragment.WEATHER_RESPONSE_KEY) as WeatherResponse?
        initViews()
        return viewBinding.root
    }

    private fun subscribeToChanges() {
        parentFragmentManager.setFragmentResultListener(WeatherFragment.WEATHER_RESPONSE_KEY,viewLifecycleOwner){_,bundle->
            weatherResponse = bundle.getSerializable(WeatherFragment.WEATHER_RESPONSE_KEY) as WeatherResponse
        }
    }

    private fun initViews() {
        with(viewBinding){
            cityNameTv.text = weatherResponse?.cityName
            temperatureTv.text = weatherResponse?.main?.temp.toString()
            humidityTv.text = weatherResponse?.main?.humidity.toString()
            pressureTv.text = weatherResponse?.main?.pressure.toString()
            speedTv.text =weatherResponse?.wind?.speed.toString()
        }
    }


    companion object{
        fun newInstance() = InfoBottomSheetFragment()
        const val TAG = "InfoBottomSheetFragment"
    }
}