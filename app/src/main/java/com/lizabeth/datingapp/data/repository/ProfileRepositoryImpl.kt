package com.lizabeth.datingapp.data.repository

import com.lizabeth.datingapp.data.datasource.AuthRemoteDataSource
import com.lizabeth.datingapp.data.datasource.FirestoreRemoteDataSource
import com.lizabeth.datingapp.data.datasource.StorageRemoteDataSource
import com.lizabeth.datingapp.data.datasource.model.FirestoreUserProperties
import com.lizabeth.datingapp.domain.profile.ProfileRepository
import com.lizabeth.datingapp.domain.profile.entity.CreateUserProfile
import com.lizabeth.datingapp.domain.profilecard.entity.CurrentProfile
import com.lizabeth.datingapp.domain.profile.entity.FirebasePicture
import com.lizabeth.datingapp.domain.profile.entity.UserPicture

class ProfileRepositoryImpl(
    private val authDataSource: AuthRemoteDataSource,
    private val storageDataSource: StorageRemoteDataSource,
    private val firestoreDataSource: FirestoreRemoteDataSource
): ProfileRepository {

    override suspend fun createUserProfile(profile: CreateUserProfile) {
        createUserProfile(authDataSource.userId, profile)
    }

    override suspend fun createUserProfile(userId: String, profile: CreateUserProfile) {
        val filenames = storageDataSource.uploadUserPictures(userId, profile.pictures)
        firestoreDataSource.createUserProfile(
            userId,
            profile.name,
            profile.birthdate,
            profile.bio,
            profile.isMale,
            profile.orientation,
            filenames.map { it.filename }
        )
    }

    override suspend fun updateProfile(
        currentProfile: CurrentProfile,
        newBio: String, newGenderIndex: Int,
        newOrientationIndex: Int,
        newPictures: List<UserPicture>
    ): CurrentProfile {

        val arePicturesEqual = currentProfile.pictures == newPictures
        val isDataEqual = currentProfile.isDataEqual(newBio, newGenderIndex, newOrientationIndex)

        if (arePicturesEqual && isDataEqual) {
            return currentProfile
        } else if (arePicturesEqual) {
            val data = currentProfile.toModifiedData(newBio, newGenderIndex, newOrientationIndex)
            firestoreDataSource.updateProfileData(data)
            return currentProfile.toModifiedProfile(
                newBio,
                newGenderIndex,
                newOrientationIndex
            )
        } else if (isDataEqual) {
            val firebasePictures = updateProfilePictures(currentProfile.pictures, newPictures)
            return currentProfile.copy(pictures = firebasePictures)
        } else {
            val data = currentProfile.toModifiedData(newBio, newGenderIndex, newOrientationIndex)
            val firebasePictures =
                updateProfileDataAndPictures(data, currentProfile.pictures, newPictures)
            return currentProfile.toModifiedProfile(
                newBio,
                newGenderIndex,
                newOrientationIndex,
                firebasePictures
            )
        }
    }

    private suspend fun updateProfilePictures(
        outdatedPictures: List<FirebasePicture>,
        updatedPictures: List<UserPicture>
    ): List<FirebasePicture> {
        val filenames = storageDataSource.updateProfilePictures(
            authDataSource.userId,
            outdatedPictures,
            updatedPictures
        )
        val updatedData =
            mapOf<String, Any>(FirestoreUserProperties.pictures to filenames.map { it.filename })
        firestoreDataSource.updateProfileData(updatedData)
        return filenames
    }

    private suspend fun updateProfileDataAndPictures(
        data: Map<String, Any>,
        outdatedPictures: List<FirebasePicture>,
        updatedPictures: List<UserPicture>
    ): List<FirebasePicture> {
        val filenames = storageDataSource.updateProfilePictures(
            authDataSource.userId,
            outdatedPictures,
            updatedPictures
        )
        val updatedData =
            data + mapOf<String, Any>(FirestoreUserProperties.pictures to filenames.map { it.filename })
        firestoreDataSource.updateProfileData(updatedData)
        return filenames
    }

}