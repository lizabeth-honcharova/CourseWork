package com.lizabeth.datingapp.domain.profilecard

import com.lizabeth.datingapp.domain.profilecard.entity.NewMatch
import com.lizabeth.datingapp.domain.profilecard.entity.Profile
import com.lizabeth.datingapp.domain.profilecard.entity.ProfileList

interface ProfileCardRepository {
    suspend fun swipeUser(profile: Profile, isLike: Boolean): NewMatch?
    suspend fun getProfiles(): ProfileList
}