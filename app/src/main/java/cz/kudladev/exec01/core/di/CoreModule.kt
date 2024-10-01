package cz.kudladev.exec01.core.di

import android.content.Context
import cz.kudladev.exec01.core.presentation.screens.inputs_screen.InputScreenViewmodel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val coreModule = module {

    single {
        androidContext().getSharedPreferences(
            "pref", Context.MODE_PRIVATE
        )
    }

    viewModel {
        InputScreenViewmodel()
    }

}