package com.example.blogapp.ui.auth

import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.blogapp.R
import com.example.blogapp.core.TakePicture
import com.example.blogapp.core.PermissionRequester
import com.example.blogapp.core.Resource
import com.example.blogapp.data.remote.auth.AuthDataSource
import com.example.blogapp.databinding.FragmentSetupProfileBinding
import com.example.blogapp.domain.auth.AuthRepoImpl
import com.example.blogapp.presentation.auth.AuthViewModel
import com.example.blogapp.presentation.auth.AuthViewModelFactory


class SetupProfileFragment : Fragment(R.layout.fragment_setup_profile) {

    private lateinit var binding: FragmentSetupProfileBinding
    private var bitmap: Bitmap? = null
    private lateinit var imageView: ImageView

    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(AuthRepoImpl(AuthDataSource()))
    }


    private val dispatchTakePicture = TakePicture(this)

    private val getPermission =
        PermissionRequester(this, android.Manifest.permission.CAMERA,
            onDenied = {
                Toast.makeText(requireContext(), "Denied", Toast.LENGTH_SHORT).show()
            },
            onRationale = {
                Toast.makeText(requireContext(), "Rational", Toast.LENGTH_SHORT).show()
            })



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetupProfileBinding.bind(view)
        imageView = binding.profileImage



        binding.profileImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) != PermissionChecker.PERMISSION_GRANTED
            ) getPermission.runWithPermission {
                Toast.makeText(requireContext(), "Granted", Toast.LENGTH_SHORT).show()
                dispatchTakePicture.onDispatchCamera { image ->
                    bitmap = image
                    imageView.setImageBitmap(image)
                }
            } else {
                dispatchTakePicture.onDispatchCamera { image ->
                    bitmap = image
                    imageView.setImageBitmap(image)
                }
            }
        }


        binding.createProfile.setOnClickListener {
            val username = binding.txtUsername.text.toString().trim()
            bitmap?.let { image ->
                if (username.isNotEmpty()) {
                    val alertDialog =
                        AlertDialog.Builder(requireContext()).setTitle("Uploading photo..").create()
                    viewModel.updateProfile(image, username)
                        .observe(viewLifecycleOwner, Observer { result ->
                            when (result) {
                                is Resource.Loading -> {
                                    alertDialog.show()
                                }
                                is Resource.Success -> {
                                    alertDialog.dismiss()
                                    findNavController().navigate(R.id.action_setupProfileFragment_to_homeScreenFragment)
                                }
                                is Resource.Failure -> {
                                    alertDialog.dismiss()
                                }
                            }
                        })
                }
            }
        }
    }

}