package cz.kudladev.exec01

import android.app.Application
import cz.kudladev.exec01.core.di.coreModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class Exec01App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@Exec01App)
            modules(coreModule)
        }

    }
}