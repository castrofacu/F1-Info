package com.f1.info.core.di

import com.f1.info.features.drivers.data.repository.DriversRepositoryImpl
import com.f1.info.features.drivers.domain.repository.DriversRepository
import com.f1.info.features.drivers.domain.usecase.GetAllDriversUseCase
import com.f1.info.features.drivers.presentation.viewmodel.DriversViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<DriversRepository> { DriversRepositoryImpl(get()) }

    factory { GetAllDriversUseCase(get()) }

    viewModel { DriversViewModel(get()) }
}
