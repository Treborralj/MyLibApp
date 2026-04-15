package com.example.mylib.data.repo

import android.content.Context
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ImageStorageManager(private val context: Context) {

    private val imageDir = File(context.filesDir, "images")

    suspend fun saveBase64Image(base64: String, type: String, id: String): String? = withContext(Dispatchers.IO) {
        try {
            //Clean the string and decode
            val pureBase64 = base64.substringAfter("base64,")
            val bytes = Base64.decode(pureBase64, Base64.DEFAULT)

            //Determine extension
            val ext = when {
                type.contains("png", ignoreCase = true) -> "png"
                type.contains("webp", ignoreCase = true) -> "webp"
                else -> "jpg"
            }

            if (!imageDir.exists()) imageDir.mkdirs()
            val file = File(imageDir, "image_$id.$ext")

            //Write file
            file.outputStream().use { it.write(bytes) }

            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getFile(path: String): File? {
        val file = File(path)
        return if (file.exists()) file else null
    }

    fun deleteImage(path: String): Boolean {
        return try {
            File(path).delete()
        } catch (e: Exception) {
            false
        }
    }

    fun saveImage(file: File): String? {
        return try {
            if (!imageDir.exists()) imageDir.mkdirs()
            val newFile = File(imageDir, "profile_${System.currentTimeMillis()}_${file.name}")
            file.copyTo(newFile, overwrite = true)
            newFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    fun getImagePathForUser(accountId: String): String? {
        // Check for common extensions
        val extensions = listOf("jpg", "png", "webp");    for (ext in extensions) {
            val file = File(imageDir, "image_$accountId.$ext")
            if (file.exists()) return file.absolutePath
        }
        return null
    }
}
