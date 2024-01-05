package com.lizabeth.datingapp.ui.main

import android.app.Application
import com.lizabeth.datingapp.ui.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@MainApp)
            modules(appModule)
        }
    }
}