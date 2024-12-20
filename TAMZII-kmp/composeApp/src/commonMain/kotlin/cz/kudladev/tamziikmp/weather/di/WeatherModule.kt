package cz.kudladev.tamziikmp.weather.di

import cz.kudladev.tamziikmp.core.remote.HttpClientFactory
import cz.kudladev.tamziikmp.weather.data.KtorWeatherClient
import cz.kudladev.tamziikmp.weather.domain.WeatherClient
import cz.kudladev.tamziikmp.weather.domain.usecase.CurrentWeather
import cz.kudladev.tamziikmp.weather.presentation.WeatherScreenViewModel
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val weatherModule = module {
    single<HttpClient>{
        HttpClientFactory().create()
    }

    single<WeatherClient> {
        KtorWeatherClient(get())
    }

    single<CurrentWeather> {
        CurrentWeather(get())
    }


    viewModel { WeatherScreenViewModel(get()) }

}