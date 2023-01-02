package com.example.blogapp.domain.home

import com.example.blogapp.core.Resource
import com.example.blogapp.data.model.Post

interface HomeScreenRepo {
    suspend fun getLatestPost(): Resource<List<Post>>
}