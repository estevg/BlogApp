package com.example.blogapp.domain.camera

import android.graphics.Bitmap
import com.example.blogapp.data.remote.camera.CameraScreenDataSource

class CameraScreenImpl(private val dataSource: CameraScreenDataSource) : CameraScreenRepo {
    override suspend fun uploadPhoto(imageBitmap: Bitmap, description: String) =
        dataSource.uploadPhoto(imageBitmap, description)
}