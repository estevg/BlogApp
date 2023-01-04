package com.example.blogapp.ui.camera

import android.Manifest
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.blogapp.R
import com.example.blogapp.core.PermissionRequester
import com.example.blogapp.core.Resource
import com.example.blogapp.core.TakePicture
import com.example.blogapp.data.remote.camera.CameraScreenDataSource
import com.example.blogapp.databinding.FragmentCameraBinding
import com.example.blogapp.domain.camera.CameraScreenImpl
import com.example.blogapp.presentation.camera.CameraViewModel
import com.example.blogapp.presentation.camera.CameraViewModelFactory


class CameraFragment : Fragment(R.layout.fragment_camera) {

    private lateinit var binding: FragmentCameraBinding
    private var bitmap: Bitmap? = null
    private lateinit var imageView: ImageView
    private val viewModel by viewModels<CameraViewModel> {
        CameraViewModelFactory(CameraScreenImpl(CameraScreenDataSource()))
    }

    private val dispatchTakePicture = TakePicture(this)

    private val getPermission = PermissionRequester(this, Manifest.permission.CAMERA, onDenied = {
        Toast.makeText(requireContext(), "Denied", Toast.LENGTH_SHORT).show()
    }, onRationale = {
        Toast.makeText(requireContext(), "Rational", Toast.LENGTH_SHORT).show()
    })


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCameraBinding.bind(view)
        imageView = binding.postImage
        getTakePicture()

        binding.btnUploadPhoto.setOnClickListener {
            val description = binding.postDescription.text.toString().trim()
            bitmap?.let { image ->
                viewModel.uploadPhoto(image, description).observe(viewLifecycleOwner, Observer { result ->
                    when(result){
                        is Resource.Loading -> {
                            Toast.makeText(
                                requireContext(),
                                "Uploading photo....",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is Resource.Success -> {
                            findNavController().navigate(R.id.action_cameraFragment_to_homeScreenFragment)
                        }
                        is Resource.Failure -> {
                            Toast.makeText(
                                requireContext(),
                                "Failure ${result.exception}.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            }

        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun getTakePicture() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) != PermissionChecker.PERMISSION_GRANTED
        ) {
            getPermission.runWithPermission {
                Toast.makeText(requireContext(), "Granted", Toast.LENGTH_SHORT).show()
                dispatchTakePicture.onDispatchCamera { image ->
                    bitmap = image
                    imageView.setImageBitmap(image)
                }
            }
        } else {
            dispatchTakePicture.onDispatchCamera { image ->
                bitmap = image
                imageView.setImageBitmap(image)
            }
        }
    }
}