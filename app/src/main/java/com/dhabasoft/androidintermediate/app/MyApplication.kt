package com.dhabasoft.androidintermediate.app

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.multidex.MultiDex
import com.google.android.play.core.splitcompat.SplitCompat
import com.zeugmasolutions.localehelper.LocaleHelper
import com.zeugmasolutions.localehelper.LocaleHelperApplicationDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class MyApplication : Application() {
    private val localeAppDelegate = LocaleHelperApplicationDelegate()

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base?.let { localeAppDelegate.attachBaseContext(it) })
        SplitCompat.install(this)
        MultiDex.install(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeAppDelegate.onConfigurationChanged(this)
    }

    override fun getApplicationContext(): Context? {
        super.getApplicationContext()?.let {
            return LocaleHelper.onAttach(it)
        }
        return super.getApplicationContext()
    }
}