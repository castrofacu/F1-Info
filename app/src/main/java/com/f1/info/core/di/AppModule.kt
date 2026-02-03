package com.f1.info.core.di

import com.f1.info.core.data.repository.DriversRepositoryImpl
import com.f1.info.core.data.repository.PositionsRepositoryImpl
import com.f1.info.core.data.repository.RacesRepositoryImpl
import com.f1.info.core.domain.repository.DriversRepository
import com.f1.info.core.domain.repository.PositionsRepository
import com.f1.info.core.domain.repository.RacesRepository
import com.f1.info.core.domain.usecase.GetDriversUseCase
import com.f1.info.core.domain.usecase.GetPositionsUseCase
import com.f1.info.core.domain.usecase.GetRacesUseCase
import com.f1.info.features.drivers.presentation.viewmodel.DriversViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<DriversRepository> { DriversRepositoryImpl(get()) }

    factory { GetDriversUseCase(get()) }

    viewModel { DriversViewModel(get()) }

    single<RacesRepository> {
        RacesRepositoryImpl(get())
    }

    factory { GetRacesUseCase(get()) }

    single<PositionsRepository> {
        PositionsRepositoryImpl(get())
    }

    factory { GetPositionsUseCase(get()) }
}
