package com.example.blogapp.data.remote.home

import com.example.blogapp.core.Resource
import com.example.blogapp.data.model.Post
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class HomeScreenDataSource {


    suspend fun getLatestPost(): Resource<List<Post>> {
        val postList = mutableListOf<Post>()
        val querySnapShop =
            FirebaseFirestore.getInstance().collection("posts").orderBy("create_at", Query.Direction.ASCENDING).get().await()
        for (post in querySnapShop.documents) {
            post.toObject(Post::class.java)?.let { fbPost ->
                fbPost.apply {
                    create_at = post.getTimestamp(
                        "created_at",
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    )?.toDate()
                }
                postList.add(fbPost)
            }
        }
        return Resource.Success(postList)
    }
}