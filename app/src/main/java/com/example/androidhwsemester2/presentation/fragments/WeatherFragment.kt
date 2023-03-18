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
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androidhwsemester2.R
import com.example.androidhwsemester2.data.local.entity.CityWeatherInfo
import com.example.androidhwsemester2.data.mappers.WeatherResponseMapper
import com.example.androidhwsemester2.data.local.repository.WeatherInfoRepository
import com.example.androidhwsemester2.data.remote.model.WeatherResponse
import com.example.androidhwsemester2.data.remote.repository.WeatherRepository
import com.example.androidhwsemester2.databinding.FragmentWeatherBinding
import com.example.androidhwsemester2.presentation.MainActivity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.Calendar
import kotlin.properties.Delegates

class WeatherFragment : Fragment(R.layout.fragment_weather) {

    private val viewBinding: FragmentWeatherBinding by viewBinding(FragmentWeatherBinding::bind)
    private var weatherRepository: WeatherRepository? = null
    private val permission = android.Manifest.permission.ACCESS_FINE_LOCATION
    private var locationManager: LocationManager? = null
    private var dbRepository: WeatherInfoRepository? = null
    private var weatherResponse: WeatherResponse? = null

    private var weatherInfo: CityWeatherInfo? by Delegates.observable(null) { _, _, newValue ->
        updateUi(newValue?.cityName ?: "error", newValue?.temperature.toString())
    }

    private fun updateUi(cityName: String, temp: String) {
        with(viewBinding) {
            cityNameTv.text = cityName
            temperatureTv.text = getString(R.string.temperature,temp.toFloat())
        }

    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherRepository = WeatherRepository()
        dbRepository = WeatherInfoRepository(requireContext())
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        initListeners()
    }
    private fun initListeners() {
        with(viewBinding) {
            byNameBtn.setOnClickListener {
                if (cityEt.editText?.text?.isNotBlank() == true) {
                    loadWithName()
                }else{
                    Toast.makeText(requireContext(),"Blank input",Toast.LENGTH_SHORT).show()
                }
            }
            byCordsBtn.setOnClickListener {
                (activity as MainActivity).requestSinglePermission(
                    permission,
                    ::permissionCallback
                )
            }
            cityNameTv.setOnClickListener {
                InfoBottomSheetFragment.newInstance(bundleOf(WEATHER_RESPONSE_KEY to weatherInfo))
                    .show(
                        childFragmentManager,
                        InfoBottomSheetFragment.TAG
                    )
            }
        }
    }

    private fun handleHttpException(throwable: Throwable) {
        var message: String = ""
        if (throwable is HttpException) {
            when (throwable.code()) {
                404 -> message = "City not found"
                400 -> message = "Incorrect city name"
            }
            Toast.makeText(
                requireContext(),
                message,
                Toast.LENGTH_SHORT
            ).show()
        } else {
            message = throwable.message.toString()
            Log.e("er", throwable.message.toString())
        }
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
        endLoading()
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
                val cache = checkCache(
                    lat = location.latitude,
                    lon = location.longitude
                )
                if (cache == null) {
                    weatherInfo = WeatherResponseMapper.mapToDB(
                        weatherRepository?.getWeatherInfoByCords(
                            lat = location.latitude,
                            lon = location.longitude
                        )
                    )
                    dbRepository?.saveCityWeatherInfo(weatherInfo ?: throw Exception())

                    Log.d("cache", "from network")
                } else {
                    weatherInfo = cache
                    Log.d("cache", "from cache")
                }
                endLoading()
            }
        }
    }

    private fun loadWithName() {
        lifecycleScope.launch(CoroutineExceptionHandler { _, throwable ->
            handleHttpException(throwable)
        }) {
            val cityName = viewBinding.cityEt.editText?.text.toString().capitalize()
            beginLoading()
            val cache = checkCache(cityName)
            if (cache == null) {
                weatherInfo =
                    WeatherResponseMapper.mapToDB(weatherRepository?.getWeatherInfoByCityName(cityName))
                dbRepository?.saveCityWeatherInfo(weatherInfo ?: throw Exception("bad response"))
                Log.d("cache", "from network")
            } else {
                weatherInfo = cache
                Log.d("cache", "from cache")
            }
            endLoading()
        }
    }

    private suspend fun checkCache(cityName: String): CityWeatherInfo? {
        val cityWeatherInfo = dbRepository?.getCityWeatherInfoByName(cityName)
        return if (cityWeatherInfo == null) {
            null // no cache
        } else if ((Calendar.getInstance().timeInMillis - cityWeatherInfo.lastSearch.time) > 60 * 1000) {
            null // cache is old
        } else {
            cityWeatherInfo //new cache
        }
    }

    private suspend fun checkCache(lat: Double, lon: Double): CityWeatherInfo? {
        val cityWeatherInfo = dbRepository?.getCityWeatherInfoByCords(lat = lat, lon = lon)
        return if (cityWeatherInfo == null) {
            null // no cache
        } else if ((Calendar.getInstance().timeInMillis - cityWeatherInfo.lastSearch.time) > CACHE_LIFETIME) {
            null // cache is old
        } else {
            cityWeatherInfo //new cache
        }
    }


    private fun beginLoading() {
        with(viewBinding) {
            cityNameTv.visibility = View.GONE
            loadingPb.visibility = View.VISIBLE
            temperatureTv.visibility = View.GONE
        }
    }

    private fun endLoading() {
        with(viewBinding) {
            cityNameTv.visibility = View.VISIBLE
            temperatureTv.visibility = View.VISIBLE
            loadingPb.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        weatherRepository = null
        locationManager = null
        weatherResponse = null
        dbRepository = null
        super.onDestroy()
    }


    companion object {
        fun newInstance() = WeatherFragment()
        const val WEATHER_RESPONSE_KEY = "WEATHER_RESPONSE_KEY"
        const val TAG = "WeatherFragment"
        const val CACHE_LIFETIME = 60 * 1000
    }
}