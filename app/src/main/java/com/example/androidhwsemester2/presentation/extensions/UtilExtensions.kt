package com.example.androidhwsemester2.presentation.extensions

import android.os.Build
import android.os.Bundle

inline fun <reified T : java.io.Serializable> Bundle.getSerializableValue(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getSerializable(key, T::class.java)
    } else {
        @Suppress("DEPRECATION") this.getSerializable(key) as T?
    }
}