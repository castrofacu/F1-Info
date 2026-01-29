package com.f1.info.di

import com.f1.info.features.drivers.data.repository.DriversRepositoryImpl
import com.f1.info.features.drivers.domain.repository.DriversRepository
import com.f1.info.features.drivers.domain.usecase.GetAllDriversUseCase
import org.koin.dsl.module

val appModule = module {
    single<DriversRepository> { DriversRepositoryImpl(get()) }
    factory { GetAllDriversUseCase(get()) }
}
