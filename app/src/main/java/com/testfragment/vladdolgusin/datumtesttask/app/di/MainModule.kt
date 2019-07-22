package com.testfragment.vladdolgusin.datumtesttask.app.di

import android.app.Application
import com.testfragment.vladdolgusin.datumtesttask.app.di.NetWork.NetworkApi
import com.testfragment.vladdolgusin.datumtesttask.data.net.MapService
import com.testfragment.vladdolgusin.datumtesttask.data.repository.MapRepositoryImpl
import com.testfragment.vladdolgusin.datumtesttask.domain.interactor.MapInteractor
import com.testfragment.vladdolgusin.datumtesttask.domain.interactor.impl.MapInteractorImpl


object MainModule {
    private lateinit var context: Application

    fun initialize(app: Application) {
        this.context = app
    }

    fun getCityIneractorImpl(): MapInteractor {
        return makeTaskInteractor()
    }

    private fun makeTaskInteractor() = MapInteractorImpl(MapRepositoryImpl())
}