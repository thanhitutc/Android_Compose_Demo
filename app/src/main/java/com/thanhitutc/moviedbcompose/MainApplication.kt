package com.thanhitutc.moviedbcompose

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application() {

    // use this to run coroutines that need a longer lifetime than the calling scope (like viewModelScope) might offer in our app
    val appScope by lazy {
        CoroutineScope(SupervisorJob())
    }

    override fun onCreate() {
        super.onCreate()
        if (isDevMode()) {
            // init timber
            Timber.plant(Timber.DebugTree())
        } else {
        }
    }
}

fun isDevMode() = BuildConfig.BUILD_TYPE != "release"
