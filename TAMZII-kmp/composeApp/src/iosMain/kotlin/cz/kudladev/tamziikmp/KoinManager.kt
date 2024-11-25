package cz.kudladev.tamziikmp

import cz.kudladev.tamziikmp.weather.di.weatherModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

fun initKoin() {
    stopKoin()
    startKoin {
        modules(weatherModule)
    }
}
