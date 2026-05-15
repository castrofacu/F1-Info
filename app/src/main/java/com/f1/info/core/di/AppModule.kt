package com.f1.info.core.di

import com.f1.info.presentation.drivers.DriversViewModel
import com.f1.info.presentation.racereplay.RaceReplayViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { DriversViewModel(get()) }
    viewModel { RaceReplayViewModel(get(), get(), get()) }
}
