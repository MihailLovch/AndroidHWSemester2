package com.example.androidhwsemester2.presentation.permissions

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat.startActivity
import com.example.androidhwsemester2.presentation.MainActivity

class PermissionsHandler(
    private val activity: MainActivity,
    var singlePermissionCallBack: ((Boolean) -> Unit)? = null
) {
    private val requestSinglePermissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        singlePermissionCallBack?.invoke(isGranted)
    }

    fun requestSinglePermission(permission: String) {
        requestSinglePermissionLauncher.launch(permission)
    }

}