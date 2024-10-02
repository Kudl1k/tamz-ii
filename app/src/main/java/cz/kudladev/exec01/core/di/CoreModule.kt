package cz.kudladev.exec01.core.di

import android.content.Context
import cz.kudladev.exec01.core.data.api.StoreApi
import cz.kudladev.exec01.core.data.api.WeatherApi
import cz.kudladev.exec01.core.presentation.screens.api_screen.APIScreenViewModel
import cz.kudladev.exec01.core.presentation.screens.graphs.GraphsScreenViewModel
import cz.kudladev.exec01.core.presentation.screens.inputs_screen.InputScreenViewmodel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val coreModule = module {

    single {
        androidContext().getSharedPreferences(
            "pref", Context.MODE_PRIVATE
        )
    }


    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(StoreApi.BASE_URL)
            .build()
            .create(StoreApi::class.java)
    }

    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(WeatherApi.BASE_URL)
            .build()
            .create(WeatherApi::class.java)
    }

    viewModel {
        InputScreenViewmodel()
    }
    viewModel {
        APIScreenViewModel(get())
    }

    viewModel{
        GraphsScreenViewModel(get()){
            androidContext()
        }
    }


}