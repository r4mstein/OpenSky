package ua.shtain.opensky.core

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import ua.shtain.opensky.BuildConfig

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}