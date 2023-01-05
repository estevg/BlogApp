package com.example.blogapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.blogapp.core.Resource
import com.example.blogapp.domain.home.HomeScreenRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

class HomeScreenViewModel(private val repo: HomeScreenRepo): ViewModel() {

    fun fetchLatestPosts() = liveData(Dispatchers.IO) {
        emit(Resource.Loading())

        kotlin.runCatching {
            repo.getLatestPost()
        }.onSuccess { flowList ->
            flowList.collect {
                emit(it)
            }
        }.onFailure { throwable ->
            emit(Resource.Failure(Exception(throwable.message)))
        }
    }
}

class HomeScreenViewModelFactory(private val repo: HomeScreenRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  modelClass.getConstructor(HomeScreenRepo::class.java).newInstance(repo)
    }
}