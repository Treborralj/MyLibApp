package com.example.mylib.ui.util

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun base64ToImageBitmap(base64String: String?): ImageBitmap? {
    if (base64String.isNullOrBlank()) return null

    return try {
        val bytes = Base64.decode(base64String, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}