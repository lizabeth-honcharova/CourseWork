package com.lizabeth.datingapp.ui.signup

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lizabeth.datingapp.data.datasource.SignInCheck
import com.lizabeth.datingapp.domain.auth.AuthRepository
import com.lizabeth.datingapp.domain.profile.ProfileRepository
import com.lizabeth.datingapp.domain.profile.entity.CreateUserProfile
import com.lizabeth.datingapp.domain.profile.entity.DevicePicture
import com.lizabeth.datingapp.extensions.filterIndex
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        SignUpUiState(
            isLoading = false,
            pictures = emptyList(),
            isUserSignedIn = false,
            errorMessage = null
        )
    )
    val uiState = _uiState.asStateFlow()

    fun removePictureAt(index: Int) {
        _uiState.update { it.copy(pictures = it.pictures.filterIndex(index)) }
    }

    fun addPicture(picture: DevicePicture) {
        _uiState.update { it.copy(pictures = it.pictures + picture) }
    }

    fun signUp(data: Intent?, profile: CreateUserProfile) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                authRepository.signInWithGoogle(data, signInCheck = SignInCheck.ENFORCE_NEW_USER)
                profileRepository.createUserProfile(profile)
                _uiState.update { it.copy(isUserSignedIn = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message)
                }
            }
        }
    }


}

data class SignUpUiState(
    val isLoading: Boolean,
    val pictures: List<DevicePicture>,
    val isUserSignedIn: Boolean,
    val errorMessage: String?
)