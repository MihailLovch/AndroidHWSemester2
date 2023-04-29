package com.example.androidhwsemester2.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.MutableCreationExtras
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androidhwsemester2.R
import com.example.androidhwsemester2.databinding.FragmentWeatherBinding
import com.example.androidhwsemester2.di.manual.DataDependency
import com.example.androidhwsemester2.di.manual.ViewModelArgsKey
import com.example.androidhwsemester2.presentation.MainActivity
import com.example.androidhwsemester2.presentation.extensions.shortToast
import com.example.androidhwsemester2.presentation.viewmodel.WeatherViewModel

class WeatherFragment : Fragment(R.layout.fragment_weather) {

    private val viewBinding: FragmentWeatherBinding by viewBinding(FragmentWeatherBinding::bind)
    private val permission = android.Manifest.permission.ACCESS_FINE_LOCATION
    private var dataDependency: DataDependency? = null
    private var locationManager: LocationManager? = null

    private val viewModel: WeatherViewModel by viewModels(extrasProducer = {
        MutableCreationExtras().apply {
//            set(ViewModelArgsKey.getWeatherByNameUseCase, dataDependency?.getWeatherByNameUseCase!!)
//            set(ViewModelArgsKey.getWeatherByCordsUseCase, dataDependency?.getWeatherByCordsUseCase!!)
        }
    }) {
        WeatherViewModel.factory
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dataDependency = DataDependency(requireContext())
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        initViews()
        observeData()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun observeData() {
        with(viewBinding) {
            viewModel.progressBarState.observe(viewLifecycleOwner) { isVisible ->
                loadingPb.isVisible = isVisible
                cityNameTv.isVisible = !isVisible
                temperatureTv.isVisible = !isVisible
            }
            viewModel.weatherInfoState.observe(viewLifecycleOwner) { weatherModel ->
                weatherModel?.let {
                    cityNameTv.text = weatherModel.cityName
                    temperatureTv.text = getString(R.string.temperature, weatherModel.temperature)
                }
            }
            viewModel.errorState.observe(viewLifecycleOwner) { message ->
                if (message.isNotBlank()) {
                    shortToast(message)
                }
            }
        }
    }


    private fun initViews() {
        with(viewBinding) {
            byNameBtn.setOnClickListener {
                if (cityEt.editText?.text?.isNotBlank() == true) {
                    viewModel.requestCityByName(cityEt.editText?.text.toString())
                } else {
                    shortToast("Blank input")
                }
            }
            byCordsBtn.setOnClickListener {
                (activity as MainActivity).requestSinglePermission(
                    permission,
                    ::permissionCallback
                )
            }
            cityNameTv.setOnClickListener {
                InfoBottomSheetFragment.newInstance(bundleOf(WEATHER_RESPONSE_KEY to viewModel.weatherInfoState.value))
                    .show(
                        childFragmentManager,
                        InfoBottomSheetFragment.TAG
                    )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun permissionCallback(isGranted: Boolean) {
        if (isGranted) {
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f) {}
            val location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            location?.let {
                viewModel.requestCityBoCords(
                    lat = location.latitude,
                    lon = location.longitude
                )
            }
        } else {
            if (shouldShowRequestPermissionRationale(permission)) {
                shortToast("Please give us permission to find out your coordinates")
            } else {
                shortToast("Please give us permissions in settings")
                val appSettingsIntent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + requireActivity().packageName)
                )
                startActivity(appSettingsIntent)
            }
        }
    }

    companion object {
        fun newInstance() = WeatherFragment()
        const val WEATHER_RESPONSE_KEY = "WEATHER_RESPONSE_KEY"
        const val TAG = "WeatherFragment"
    }

}