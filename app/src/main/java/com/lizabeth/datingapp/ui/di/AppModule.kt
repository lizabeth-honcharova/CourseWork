package com.lizabeth.datingapp.ui.di

import com.lizabeth.datingapp.data.di.dataModule
import org.koin.dsl.module

val appModule = module {
    includes(dataModule)
    includes(presentationModule)
}