package com.example.blogapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.blogapp.R
import com.example.blogapp.core.Resource
import com.example.blogapp.data.remote.home.HomeScreenDataSource
import com.example.blogapp.databinding.FragmentHomeScreenBinding
import com.example.blogapp.domain.home.HomeScreenImpl
import com.example.blogapp.presentation.HomeScreenViewModel
import com.example.blogapp.presentation.HomeScreenViewModelFactory
import com.example.blogapp.ui.home.adapter.HomeScreenAdapter


class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    private lateinit var binding: FragmentHomeScreenBinding
    private val viewModel by viewModels<HomeScreenViewModel> {
        HomeScreenViewModelFactory(HomeScreenImpl(HomeScreenDataSource()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeScreenBinding.bind(view)

        viewModel.fetchLatestPosts().observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE

                    binding.rvHome.adapter = HomeScreenAdapter(result.data)
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "Ocurrio un error: ${result.exception}", Toast.LENGTH_LONG).show()
                }
            }
        })

    }

}