package com.lizabeth.datingapp.data.di

import com.lizabeth.datingapp.data.datasource.AuthRemoteDataSource
import com.lizabeth.datingapp.data.datasource.FirestoreRemoteDataSource
import com.lizabeth.datingapp.data.datasource.StorageRemoteDataSource
import com.lizabeth.datingapp.data.repository.*
import com.lizabeth.datingapp.domain.auth.AuthRepository
import com.lizabeth.datingapp.domain.match.MatchRepository
import com.lizabeth.datingapp.domain.message.MessageRepository
import com.lizabeth.datingapp.domain.profile.ProfileRepository
import com.lizabeth.datingapp.domain.profilecard.ProfileCardRepository
import org.koin.dsl.module

val dataModule = module {

    //Data sources
    single { AuthRemoteDataSource() }
    single { FirestoreRemoteDataSource() }
    single { StorageRemoteDataSource() }

    //Repositories
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<MatchRepository> { MatchRepositoryImpl(get(),get(),get()) }
    single<MessageRepository> { MessageRepositoryImpl(get()) }
    single<ProfileCardRepository> { ProfileCardRepositoryImpl(get(), get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get(),get(),get()) }
}