package com.example.blogapp.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.blogapp.R
import com.example.blogapp.core.Resource
import com.example.blogapp.data.remote.auth.AuthDataSource
import com.example.blogapp.databinding.FragmentRegisterBinding
import com.example.blogapp.domain.auth.AuthRepoImpl
import com.example.blogapp.presentation.auth.AuthViewModel
import com.example.blogapp.presentation.auth.AuthViewModelFactory


class RegisterFragment : Fragment(R.layout.fragment_register) {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(AuthRepoImpl(AuthDataSource()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        getSignUpInfo()
    }

    private fun getSignUpInfo() {
        binding.btnSignup.setOnClickListener {

            val username = binding.editTextUsername.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val confirmPassword = binding.editTextConfirmPassword.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()

            if (validateUserData(
                    password,
                    confirmPassword,
                    username,
                    email
                )
            ) return@setOnClickListener

            createUser(email, username, password)
        }
    }

    private fun createUser(email: String, username: String, password: String) {
        viewModel.signUp(email, password, username).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSignup.isEnabled = false
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_registerFragment_to_setupProfileFragment)
                }
                is Resource.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSignup.isEnabled = true

                }
            }
        })
    }

    private fun validateUserData(
        password: String,
        confirmPassword: String,
        username: String,
        email: String
    ): Boolean {
        if (password != confirmPassword) {
            binding.editTextPassword.error = "Password does not match"
            binding.editTextConfirmPassword.error = "Password does not match"
            return true
        }

        if (username.isEmpty()) {
            binding.editTextUsername.error = "Username is empty"
        }

        if (email.isEmpty()) {
            binding.editTextUsername.error = "Email is empty"
        }

        if (password.isEmpty()) {
            binding.editTextUsername.error = "Password is empty"
        }

        if (confirmPassword.isEmpty()) {
            binding.editTextUsername.error = "Confirm Password is empty"
        }

        return false

    }

}