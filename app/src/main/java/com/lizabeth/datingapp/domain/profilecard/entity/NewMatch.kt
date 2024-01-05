package com.lizabeth.datingapp.domain.profilecard.entity

import android.net.Uri

data class NewMatch(
    val id: String,
    val userId: String,
    val userName: String,
    val userPictures: List<Uri>
)
