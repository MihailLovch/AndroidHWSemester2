package com.example.androidhwsemester2.presentation.extensions

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import retrofit2.HttpException

inline fun <reified T : java.io.Serializable> Bundle.getSerializableValue(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getSerializable(key, T::class.java)
    } else {
        @Suppress("DEPRECATION") this.getSerializable(key) as T?
    }
}

fun Throwable.handleException(): String{
    var message: String = ""
    if (this is HttpException) {
        when (code()) {
            404 -> message = "City not found"
            400 -> message = "Incorrect city name"
        }
    } else {
        message = this.message.toString()
    }
    return message
}
fun Fragment.shortToast(message: String){
    Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
}