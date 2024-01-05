package com.lizabeth.datingapp.data.repository

import com.lizabeth.datingapp.data.datasource.FirestoreRemoteDataSource
import com.lizabeth.datingapp.domain.message.MessageRepository

class MessageRepositoryImpl(private val firestoreDataSource : FirestoreRemoteDataSource):
    MessageRepository {

    override fun getMessages(matchId: String) = firestoreDataSource.getMessages(matchId)

    override suspend fun sendMessage(matchId: String, text: String) {
        firestoreDataSource.sendMessage(matchId, text)
    }
}