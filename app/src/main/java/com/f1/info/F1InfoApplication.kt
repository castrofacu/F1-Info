package com.f1.info

import android.app.Application
import com.f1.info.di.appModule
import com.f1.info.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class F1InfoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@F1InfoApplication)
            modules(appModule, networkModule)
        }
    }
}
