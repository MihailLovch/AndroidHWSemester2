package com.example.androidhwsemester2.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.example.androidhwsemester2.R
import com.example.androidhwsemester2.presentation.fragments.WeatherFragment
import com.example.androidhwsemester2.presentation.permissions.PermissionsHandler

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val fragmentContainerId = R.id.main_fragments_container
    private var permissionsHandler: PermissionsHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permissionsHandler = PermissionsHandler(this)
        supportFragmentManager.commit {
            add(
                fragmentContainerId,
                WeatherFragment.newInstance(),
                WeatherFragment.TAG
            )
        }
    }

    fun requestSinglePermission(permission: String, callBack: ((Boolean) -> Unit)? = null) {
        callBack?.let {
            permissionsHandler?.singlePermissionCallBack = callBack
        }
        permissionsHandler?.requestSinglePermission(permission)
    }
}