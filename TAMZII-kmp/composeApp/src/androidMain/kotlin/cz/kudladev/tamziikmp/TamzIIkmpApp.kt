package cz.kudladev.tamziikmp

import android.app.Application
import cz.kudladev.tamziikmp.weather.di.weatherModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class TamzIIkmpApp: Application() {
    companion object {
        lateinit var INSTANCE: TamzIIkmpApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        startKoin {
            androidContext(this@TamzIIkmpApp)
            androidLogger()
            modules(weatherModule, permissionsModule)
        }
    }

}