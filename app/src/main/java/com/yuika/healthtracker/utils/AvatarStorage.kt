package com.yuika.healthtracker.utils

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.File

fun copyAvatarToAppStorage(context: Context, uri: Uri): String {
    val extension = MimeTypeMap.getSingleton()
        .getExtensionFromMimeType(context.contentResolver.getType(uri)) ?: "jpg"

    val dir = File(context.filesDir, "avatars")
    if (!dir.exists()) dir.mkdirs()

    val file = File(dir, "avatar_${System.currentTimeMillis()}.${extension}")
    val input = context.contentResolver.openInputStream(uri)
        ?: throw IllegalArgumentException("Cannot open selected image")

    input.use { source ->
        file.outputStream().use { target -> source.copyTo(target) }
    }

    return file.absolutePath
}
