package com.dhabasoft.weathermap.app

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.multidex.MultiDex
import com.google.android.play.core.splitcompat.SplitCompat
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class MyApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
        MultiDex.install(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun getApplicationContext(): Context? {
        return super.getApplicationContext()
    }
}