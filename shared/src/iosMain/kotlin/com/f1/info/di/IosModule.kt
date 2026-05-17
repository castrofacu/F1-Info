package com.f1.info.di

import com.f1.info.presentation.drivers.DriversViewModel
import com.f1.info.presentation.racereplay.RaceReplayViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.mp.KoinPlatformTools

val iosModule = module {
    factory { DriversViewModel(get()) }
    factory { RaceReplayViewModel(get(), get(), get()) }
}

fun initKoinIos() {
    if (KoinPlatformTools.defaultContext().getOrNull() != null) return
    startKoin {
        modules(sharedModule, iosModule)
    }
}
