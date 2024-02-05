package com.example.blogapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.blogapp.core.Resource
import com.example.blogapp.data.model.Post
import com.example.blogapp.domain.home.HomeScreenRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val repo: HomeScreenRepo): ViewModel() {

   /* fun fetchLatestPosts() = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        kotlin.runCatching {
            repo.getLatestPost()
        }.onSuccess { flowList ->
            emit(flowList)
        }.onFailure { throwable ->
            emit(Resource.Failure(Exception(throwable.message)))
        }
    }*/

    // with Flow coroutine builder
    val latestPosts: StateFlow<Resource<List<Post>>> = flow {
        kotlin.runCatching {
            repo.getLatestPost()
        }.onFailure { throwable ->
            emit(Resource.Failure(Exception(throwable)))
        }.onSuccess { postList ->
            emit(postList)
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000), // Or Lazily because it's a one-shot
        initialValue = Resource.Loading()
    )

    // Second form

    private val posts = MutableStateFlow<Resource<List<Post>>>(Resource.Loading())

    fun fetchPosts() = viewModelScope.launch {
        kotlin.runCatching {
            repo.getLatestPost()
        }.onSuccess { resultPostList ->
            posts.value = resultPostList
        }.onFailure {throwable ->
            posts.value = Resource.Failure(Exception(throwable))
        }
    }

    fun getPost(): StateFlow<Resource<List<Post>>> = posts


    fun registerLikeButtonState(postId: String, liked: Boolean) = liveData(viewModelScope.coroutineContext + Dispatchers.Main) {
        kotlin.runCatching {
            repo.registerLikeButtonState(postId, liked)
        }.onSuccess {
            emit(Resource.Success(Unit))
        }.onFailure {
            emit(Resource.Failure(Exception(it.message)))

        }
    }
}

class HomeScreenViewModelFactory(private val repo: HomeScreenRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  modelClass.getConstructor(HomeScreenRepo::class.java).newInstance(repo)
    }
}