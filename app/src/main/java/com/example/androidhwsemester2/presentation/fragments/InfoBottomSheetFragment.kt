package com.example.androidhwsemester2.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.androidhwsemester2.R
import com.example.androidhwsemester2.databinding.DialogFragmentInfoBinding
import com.example.androidhwsemester2.presentation.extensions.getSerializableValue
import com.example.androidhwsemester2.presentation.model.WeatherDataModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InfoBottomSheetFragment : BottomSheetDialogFragment(R.layout.dialog_fragment_info) {

    private var _viewBinding: DialogFragmentInfoBinding? = null
    private val viewBinding: DialogFragmentInfoBinding get() = _viewBinding!!
    private var cityWeatherInfo: WeatherDataModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = DialogFragmentInfoBinding.inflate(inflater)
        cityWeatherInfo = arguments?.getSerializableValue(WeatherFragment.WEATHER_RESPONSE_KEY)
        initViews()
        return viewBinding.root
    }

    private fun initViews() {
        with(viewBinding) {
            Glide
                .with(requireContext())
                .load(getString(R.string.icon_uri, cityWeatherInfo?.iconId))
                .into(weatherIconIv)
            cityNameTv.text = getString(R.string.city_name, cityWeatherInfo?.cityName)
            temperatureTv.text = getString(R.string.temperature, cityWeatherInfo?.temperature)
            humidityTv.text = getString(R.string.humidity, cityWeatherInfo?.humidity)
            pressureTv.text = getString(R.string.pressure, cityWeatherInfo?.pressure)
            speedTv.text = getString(R.string.wind_speed, cityWeatherInfo?.windSpeed)
        }
    }

    override fun onDestroy() {
        _viewBinding = null
        cityWeatherInfo = null
        super.onDestroy()
    }


    companion object {
        fun newInstance(bundle: Bundle) = InfoBottomSheetFragment().apply {
            arguments = bundle
        }

        const val TAG = "InfoBottomSheetFragment"
    }
}