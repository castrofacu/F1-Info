package com.f1.info.core.di

import com.f1.info.features.drivers.presentation.viewmodel.DriversViewModel
import com.f1.info.features.racereplay.presentation.viewmodel.RaceReplayViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { DriversViewModel(get()) }
    viewModel { RaceReplayViewModel(get(), get(), get()) }
}
