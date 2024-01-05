package com.lizabeth.datingapp.domain.auth

import android.content.Intent
import com.lizabeth.datingapp.data.datasource.SignInCheck
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val isUserSignedIn: Boolean
    val userId: String
    suspend fun signInWithGoogle(data: Intent?, signInCheck: SignInCheck = SignInCheck.ALL_USERS): FirebaseUser
    fun signOut()
}