package com.lizabeth.datingapp.ui.di

import com.lizabeth.datingapp.ui.chat.ChatViewModel
import com.lizabeth.datingapp.ui.editprofile.EditProfileViewModel
import com.lizabeth.datingapp.ui.home.HomeViewModel
import com.lizabeth.datingapp.ui.login.LoginViewModel
import com.lizabeth.datingapp.ui.matchlist.MatchListViewModel
import com.lizabeth.datingapp.ui.newmatch.NewMatchViewModel
import com.lizabeth.datingapp.ui.signup.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    //View models
    viewModel { ChatViewModel(get()) }
    viewModel { NewMatchViewModel(get()) }
    viewModel { EditProfileViewModel(get(), get()) }
    viewModel { SignUpViewModel(get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { MatchListViewModel(get()) }
}