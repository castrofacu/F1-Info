package com.f1.info.di

import com.f1.info.presentation.drivers.DriversViewModel
import com.f1.info.presentation.racereplay.RaceReplayViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class ViewModelHelper : KoinComponent {

    fun driversViewModel(): DriversViewModel = get()

    fun raceReplayViewModel(): RaceReplayViewModel = get()
}
