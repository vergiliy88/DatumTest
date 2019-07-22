package com.testfragment.vladdolgusin.datumtesttask.app.di

import android.app.Application
import com.testfragment.vladdolgusin.datumtesttask.app.di.NetWork.NetworkApi

object DI {
    fun initialize(app: Application){
        MainModule.initialize(app)
        NetworkApi.initialize()
    }
}