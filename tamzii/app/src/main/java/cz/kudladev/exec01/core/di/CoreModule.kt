package cz.kudladev.exec01.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import cz.kudladev.exec01.core.data.api.GeoApi
import cz.kudladev.exec01.core.data.api.LevelsAPI
import cz.kudladev.exec01.core.data.api.StoreApi
import cz.kudladev.exec01.core.data.api.WeatherApi
import cz.kudladev.exec01.core.presentation.screens.api_screen.APIScreenViewModel
import cz.kudladev.exec01.core.presentation.screens.face_recognition.FaceRecognitionViewModel
import cz.kudladev.exec01.core.presentation.screens.weather.WeatherScreenViewModel
import cz.kudladev.exec01.core.presentation.screens.investment_calculator.InvestmentCalculatorViewModel
import cz.kudladev.exec01.core.presentation.screens.scanner_screen.ScannerScreenViewModel
import cz.kudladev.exec01.core.presentation.screens.sokoban.SokobanViewModel
import cz.kudladev.exec01.core.presentation.screens.sokoban.db.GameDatabase
import org.koin.android.ext.koin.androidApplication
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

    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(GeoApi.BASE_URL)
            .build()
            .create(GeoApi::class.java)
    }

    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(LevelsAPI.BASE_URL)
            .build()
            .create(LevelsAPI::class.java)
    }

    single<DataStore<Preferences>> {
        androidContext().dataStore
    }

    single {
        Room.databaseBuilder(
            context = androidContext(),
            GameDatabase::class.java, "game_database"
        ).build()
    }

    single {
        get<GameDatabase>().levelDao()
    }

    single<Context>{
        androidApplication()
    }

    viewModel {
        InvestmentCalculatorViewModel(
            get<DataStore<Preferences>>(),
            get<Context>()
        )
    }
    viewModel {
        APIScreenViewModel(get())
    }

    viewModel{
        ScannerScreenViewModel()
    }

    viewModel{
        WeatherScreenViewModel(
            weatherApi = get(),
            geoApi = get()
        ){
            androidContext()
        }
    }

    viewModel{
        SokobanViewModel(
            levelsAPI = get(),
            levelDao = get()
        )
    }

    viewModel{
        FaceRecognitionViewModel()
    }


}