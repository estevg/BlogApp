package com.example.blogapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.blogapp.core.hide
import com.example.blogapp.core.show
import com.example.blogapp.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        observerDestinationChange()
    }

    private fun observerDestinationChange() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.loginFragment -> {
                    binding.bottomNavigationView.hide()
                }
                R.id.setupProfileFragment -> {
                    binding.bottomNavigationView.hide()
                }
                R.id.registerFragment -> {
                    binding.bottomNavigationView.hide()
                }
                else -> {
                    binding.bottomNavigationView.show()
                }
            }
        }
    }

}

