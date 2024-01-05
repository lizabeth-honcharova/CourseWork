package com.lizabeth.datingapp.domain.profile

import com.lizabeth.datingapp.domain.profile.entity.CreateUserProfile
import com.lizabeth.datingapp.domain.profilecard.entity.CurrentProfile
import com.lizabeth.datingapp.domain.profile.entity.UserPicture

interface ProfileRepository {
    suspend fun createUserProfile(profile: CreateUserProfile)
    suspend fun createUserProfile(userId: String, profile: CreateUserProfile)
    suspend fun updateProfile(currentProfile: CurrentProfile, newBio: String, newGenderIndex: Int, newOrientationIndex: Int, newPictures: List<UserPicture>): CurrentProfile
}