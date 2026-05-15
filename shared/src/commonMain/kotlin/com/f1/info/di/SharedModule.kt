package com.f1.info.di

import com.f1.info.data.remote.OpenF1Client
import com.f1.info.data.repository.DriversRepositoryImpl
import com.f1.info.data.repository.PositionsRepositoryImpl
import com.f1.info.data.timeline.RaceTimelineProcessor
import com.f1.info.domain.repository.DriversRepository
import com.f1.info.domain.repository.PositionsRepository
import com.f1.info.domain.usecase.BuildRaceTimelineUseCase
import com.f1.info.domain.usecase.GetDriversUseCase
import com.f1.info.domain.usecase.GetPositionsUseCase
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val sharedModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            install(Logging) {
                level = LogLevel.INFO
            }
        }
    }

    single { OpenF1Client(get()) }

    single<DriversRepository> { DriversRepositoryImpl(get()) }
    single<PositionsRepository> { PositionsRepositoryImpl(get()) }

    single { GetDriversUseCase(get()) }
    single { GetPositionsUseCase(get()) }
    single<BuildRaceTimelineUseCase> { RaceTimelineProcessor() }
}
