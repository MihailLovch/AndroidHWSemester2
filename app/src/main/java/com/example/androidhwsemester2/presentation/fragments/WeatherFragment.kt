package com.example.androidhwsemester2.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androidhwsemester2.R
import com.example.androidhwsemester2.data.model.WeatherResponse
import com.example.androidhwsemester2.data.repository.WeatherRepository
import com.example.androidhwsemester2.databinding.FragmentWeatherBinding
import com.example.androidhwsemester2.presentation.MainActivity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import kotlin.properties.Delegates

class WeatherFragment : Fragment(R.layout.fragment_weather) {

    private val viewBinding: FragmentWeatherBinding by viewBinding(FragmentWeatherBinding::bind)
    private var weatherRepository: WeatherRepository? = null
    private val permission = android.Manifest.permission.ACCESS_FINE_LOCATION
    private var locationManager: LocationManager? = null
    private var weatherResponse: WeatherResponse? by Delegates.observable(null) { _, _, newValue ->
        updateUi(newValue?.cityName.toString(), newValue?.main?.temp.toString())
        parentFragmentManager.setFragmentResult(
            WEATHER_RESPONSE_KEY,
            bundleOf(WEATHER_RESPONSE_KEY to newValue)
        )
    }

    private fun updateUi(cityName: String, temp: String) {
        with(viewBinding) {
            cityNameTv.text = cityName
            temperatureTv.text = temp
        }

    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherRepository = WeatherRepository()
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        initListeners()
    }

    private fun beginLoading() {
        with(viewBinding) {
            cityNameTv.visibility = View.GONE
            loadingPb.visibility = View.VISIBLE
        }
    }

    private fun endLoading() {
        with(viewBinding) {
            cityNameTv.visibility = View.VISIBLE
            loadingPb.visibility = View.GONE
        }
    }

    private fun handleHttpException(throwable: Throwable) {
        if (throwable is HttpException) {
            var message: String = ""
            when (throwable.code()) {
                404 -> message = "City not found"
                400 -> message = "Incorrect city name"
            }
            Toast.makeText(
                requireContext(),
                message,
                Toast.LENGTH_SHORT
            ).show()
            endLoading()
        } else {
            Log.e("er", throwable.message.toString())
        }
    }


    @SuppressLint("MissingPermission")
    private fun permissionCallback(isGranted: Boolean) {
        if (isGranted) {
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f) {}
            loadDataByCords()
        } else {
            if (shouldShowRequestPermissionRationale(permission)) {
                Toast.makeText(
                    requireContext(),
                    "Please give us permission to find out your coordinates",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please give us permissions in settings",
                    Toast.LENGTH_SHORT
                ).show()
                val appSettingsIntent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + requireActivity().packageName)
                )
                startActivity(appSettingsIntent)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun loadDataByCords() {
        lifecycleScope.launch(Dispatchers.Main + CoroutineExceptionHandler { _, throwable ->
            handleHttpException(
                throwable
            )
        }) {
            beginLoading()
            val location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            location?.let {
                weatherResponse = weatherRepository?.getWeatherInfoByCords(
                    lat = location.latitude,
                    lon = location.longitude
                )
            }
            endLoading()
        }
    }

    private fun initListeners() {
        with(viewBinding) {
            byNameBtn.setOnClickListener {
                lifecycleScope.launch(CoroutineExceptionHandler { _, throwable ->
                    handleHttpException(throwable)
                }) {
                    beginLoading()
                    weatherResponse =
                        weatherRepository?.getWeatherInfoByCityName(cityEt.editText?.text.toString())
                    endLoading()
                }
            }
            byCordsBtn.setOnClickListener {
                (activity as MainActivity).requestSinglePermission(
                    permission,
                    ::permissionCallback
                )
            }
            cityNameTv.setOnClickListener {
                InfoBottomSheetFragment.newInstance()
                    .show(
                        childFragmentManager,
                        InfoBottomSheetFragment.TAG
                    )
            }
        }
    }

    companion object {
        fun newInstance() = WeatherFragment()
        const val WEATHER_RESPONSE_KEY = "WEATHER_RESPONSE_KEY"
        const val TAG = "WeatherFragment"
    }
}