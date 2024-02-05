package com.example.blogapp.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.blogapp.R
import com.example.blogapp.core.Resource
import com.example.blogapp.core.hide
import com.example.blogapp.core.show
import com.example.blogapp.data.model.Post
import com.example.blogapp.data.remote.home.HomeScreenDataSource
import com.example.blogapp.databinding.FragmentHomeScreenBinding
import com.example.blogapp.domain.home.HomeScreenImpl
import com.example.blogapp.presentation.HomeScreenViewModel
import com.example.blogapp.presentation.HomeScreenViewModelFactory
import com.example.blogapp.ui.home.adapter.HomeScreenAdapter
import com.example.blogapp.ui.home.adapter.OnPostClickListener
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class HomeScreenFragment : Fragment(R.layout.fragment_home_screen), OnPostClickListener {

    private lateinit var binding: FragmentHomeScreenBinding
    private val viewModel by viewModels<HomeScreenViewModel> {
        HomeScreenViewModelFactory(HomeScreenImpl(HomeScreenDataSource()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeScreenBinding.bind(view)


        viewModel.fetchPosts()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getPost().collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                            binding.progressBar.show()
                        }
                        is Resource.Success -> {
                            binding.progressBar.hide()
                            if (result.data.isEmpty()) {
                                binding.emptyContainer.show()
                                return@collect
                            } else {
                                binding.emptyContainer.hide()
                            }

                            binding.rvHome.adapter = HomeScreenAdapter(result.data, this@HomeScreenFragment)
                        }
                        is Resource.Failure -> {
                            binding.progressBar.hide()
                            Toast.makeText(
                                requireContext(),
                                "Ocurrio un error: ${result.exception}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onLikeButtonClick(post: Post, liked: Boolean) {
        viewModel.registerLikeButtonState(post.id, liked).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    Log.d("Like transaction", "Loading")
                }
                is Resource.Success -> {
                    Log.d("Like transaction", "Success")

                }
                is Resource.Failure -> {
                    Log.d("Like transaction", "Failure")
                }
            }
        })
    }
}