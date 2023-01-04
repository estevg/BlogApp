package com.example.blogapp.domain.camera

import android.graphics.Bitmap

interface CameraScreenRepo {
    suspend fun uploadPhoto(imageBitmap: Bitmap, description: String)
}