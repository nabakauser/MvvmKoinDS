package com.example.mvvmkoinds.application

import android.app.Application
import com.example.mvvmkoinds.di.ConfigurationClass
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class mvvmKoinDsApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@mvvmKoinDsApplication)
            modules(ConfigurationClass.modules())
        }
    }
}