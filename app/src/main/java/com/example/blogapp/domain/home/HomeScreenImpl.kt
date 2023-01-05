package com.example.blogapp.domain.home

import com.example.blogapp.core.Resource
import com.example.blogapp.data.model.Post
import com.example.blogapp.data.remote.home.HomeScreenDataSource
import kotlinx.coroutines.flow.Flow

class HomeScreenImpl(private val dataSource: HomeScreenDataSource): HomeScreenRepo {
    override suspend fun getLatestPost(): Flow<Resource<List<Post>>> = dataSource.getLatestPost()
}