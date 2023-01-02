package com.example.blogapp.domain.auth

import com.google.firebase.auth.FirebaseUser

interface LoginRepo {
    suspend fun signIn(email: String, password: String): FirebaseUser?
}