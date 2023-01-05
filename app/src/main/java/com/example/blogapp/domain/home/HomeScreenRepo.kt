package com.example.blogapp.domain.home

import com.example.blogapp.core.Resource
import com.example.blogapp.data.model.Post
import kotlinx.coroutines.flow.Flow

interface HomeScreenRepo {
    suspend fun getLatestPost(): Flow<Resource<List<Post>>>
}