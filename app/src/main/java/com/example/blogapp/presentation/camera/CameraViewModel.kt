package com.example.blogapp.presentation.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.blogapp.core.Resource
import com.example.blogapp.domain.camera.CameraScreenRepo
import kotlinx.coroutines.Dispatchers

class CameraViewModel(private val repo: CameraScreenRepo): ViewModel() {

    fun uploadPhoto(imageBitmap: Bitmap, description: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repo.uploadPhoto(imageBitmap, description)))
        } catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

}

class CameraViewModelFactory(private val repo: CameraScreenRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CameraViewModel(repo) as T
    }
}

