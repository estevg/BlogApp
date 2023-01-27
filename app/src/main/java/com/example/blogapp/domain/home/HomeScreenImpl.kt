package com.example.blogapp.domain.home

import com.example.blogapp.core.Resource
import com.example.blogapp.data.model.Post
import com.example.blogapp.data.remote.home.HomeScreenDataSource

class HomeScreenImpl(private val dataSource: HomeScreenDataSource) : HomeScreenRepo {
    override suspend fun getLatestPost(): Resource<List<Post>> = dataSource.getLatestPost()
    override suspend fun registerLikeButtonState(postId: String, liked: Boolean) =
        dataSource.registerLikeButtonState(postId, liked)
}