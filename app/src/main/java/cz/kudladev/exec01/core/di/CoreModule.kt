package cz.kudladev.exec01.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
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

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "input_screen_preferences")

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

    single<DataStore<Preferences>> {
        androidContext().dataStore
    }

    viewModel {
        InputScreenViewmodel(
            get<DataStore<Preferences>>()
        )
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