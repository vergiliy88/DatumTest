package com.testfragment.vladdolgusin.datumtesttask.app

import android.app.Application
import com.testfragment.vladdolgusin.datumtesttask.app.di.DI

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        DI.initialize(this)
    }
}