package com.lizabeth.datingapp.data.datasource

import android.content.Intent
import android.util.Log
import com.lizabeth.datingapp.data.datasource.exception.AuthException
import com.lizabeth.datingapp.extensions.getTaskResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class AuthRemoteDataSource {

    val isUserSignedIn: Boolean
        get() = FirebaseAuth.getInstance().currentUser != null

    val userId: String
        get() = FirebaseAuth.getInstance().currentUser?.uid ?: throw AuthException("User not logged in")

    suspend fun signInWithGoogle(data: Intent?, signInCheck: SignInCheck = SignInCheck.ALL_USERS): FirebaseUser {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data ?: throw AuthException("No intent data found"))
        val account = task.getTaskResult()

        if(signInCheck != SignInCheck.ALL_USERS){
            val email = account.email ?: throw AuthException("No email found")
            Log.e("LOGIN ERROR", email)
            Log.e("LOGIN ERROR", isUserSignedIn.toString())
            //It throws an exception if the user already exists
            val isNewUser = isNewUser(email)
            if (isNewUser && signInCheck == SignInCheck.ENFORCE_EXISTING_USER) {
                throw AuthException("User doesn't exist")
                signOut()
            }

            else if (!isNewUser && signInCheck == SignInCheck.ENFORCE_NEW_USER) throw AuthException("User already exists")
        }

        val idToken = account.idToken ?: throw AuthException("No id token found")
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val authResult  = FirebaseAuth.getInstance().signInWithCredential(credential).getTaskResult()
        return authResult?.user ?: throw AuthException("User is empty")
    }

    fun signOut(){
        FirebaseAuth.getInstance().signOut()
    }

    private suspend fun isNewUser(email: String): Boolean{
        Log.e("LOGIN ERROR", email)
        val methods = FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email).getTaskResult()
        Log.e("LOGIN ERROR", methods.signInMethods.toString())
        val signInMethods = methods.signInMethods ?: throw AuthException("No sign in methods found")

        return signInMethods.isEmpty()
    }
}

enum class SignInCheck{
    ENFORCE_NEW_USER,
    ENFORCE_EXISTING_USER,
    ALL_USERS
}

