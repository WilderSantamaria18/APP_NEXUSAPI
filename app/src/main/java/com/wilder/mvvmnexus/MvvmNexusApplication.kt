package com.wilder.mvvmnexus

import android.app.Application
import com.wilder.mvvmnexus.utils.ThemeManager

class MvvmNexusApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ThemeManager.init(this)
    }
}
