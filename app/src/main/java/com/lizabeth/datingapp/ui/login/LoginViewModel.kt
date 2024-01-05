package com.lizabeth.datingapp.ui.login

import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lizabeth.datingapp.data.datasource.SignInCheck
import com.lizabeth.datingapp.domain.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository): ViewModel() {
    private val _uiState = MutableStateFlow(
        LoginViewState(
            isLoading = true,
            isUserSignedIn = false,
            errorMessage = null
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        checkLoginState()
    }

    private fun checkLoginState() {
        _uiState.update {
            if(authRepository.isUserSignedIn){
                it.copy(isUserSignedIn = true)
            } else {
                it.copy(isLoading = false)
            }
        }
    }

    fun signIn(activityResult: ActivityResult){

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                authRepository.signInWithGoogle(activityResult.data, signInCheck = SignInCheck.ENFORCE_EXISTING_USER)
                _uiState.update { it.copy(isUserSignedIn = true) }
            } catch (e: Exception) {
                if(authRepository.isUserSignedIn){
                    authRepository.signOut()
                }
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message)
                }
                e.message?.let { Log.e("LOGIN ERROR", it) }
            }
        }
    }
}

data class LoginViewState(val isLoading: Boolean, val isUserSignedIn: Boolean, val errorMessage: String?)