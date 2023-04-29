package com.example.androidhwsemester2.presentation

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.androidhwsemester2.R
import com.example.androidhwsemester2.presentation.fragments.WeatherPagerFragment
import com.example.androidhwsemester2.presentation.fragments.debug.DebugFragment
import com.example.androidhwsemester2.presentation.permissions.PermissionsHandler
import com.google.firebase.messaging.FirebaseMessaging
import kotlin.math.sqrt

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val fragmentContainerId = R.id.main_fragments_container
    private var permissionsHandler: PermissionsHandler? = null
    private var sensorManager: SensorManager? = null
    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            val acceleration =
                9.8f + sqrt((x * x + y * y + z * z).toDouble()).toFloat() - SensorManager.GRAVITY_EARTH
            if (acceleration > 20) {
                supportFragmentManager.commit {
                    replace(
                        fragmentContainerId,
                        DebugFragment.newInstance(),
                        DebugFragment.TAG
                    )
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        permissionsHandler = PermissionsHandler(this)


        supportFragmentManager.commit {
            add(
                fragmentContainerId,
                WeatherPagerFragment.newInstance(),
                WeatherPagerFragment.TAG
            )
        }
    }

    override fun onPause() {
        unregisterShakingListener()
        super.onPause()
    }

    override fun onResume() {
        registerShakingListener()
        super.onResume()
    }

    fun registerShakingListener() {
        sensorManager?.registerListener(
            sensorListener,
            sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    fun unregisterShakingListener() {
        sensorManager?.unregisterListener(sensorListener)
    }

    fun requestSinglePermission(permission: String, callBack: ((Boolean) -> Unit)? = null) {
        callBack?.let {
            permissionsHandler?.singlePermissionCallBack = callBack
        }
        permissionsHandler?.requestSinglePermission(permission)
    }
}