package com.example.mylib.ui.util

import android.content.Context
import android.net.Uri
import java.io.File

fun uriToFile(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
        ?: throw IllegalArgumentException("Could not open selected image")

    val tempFile = File.createTempFile("profile_pic_", ".jpg", context.cacheDir)

    tempFile.outputStream().use { output ->
        inputStream.use { input ->
            input.copyTo(output)
        }
    }

    return tempFile
}