package com.testfragment.vladdolgusin.datumtesttask.app.presentor.Impl

import com.testfragment.vladdolgusin.datumtesttask.app.data.City
import com.testfragment.vladdolgusin.datumtesttask.app.di.MainModule
import com.testfragment.vladdolgusin.datumtesttask.app.fragment.map.MapFragmentContract
import com.testfragment.vladdolgusin.datumtesttask.app.presentor.MapPresentor
import com.testfragment.vladdolgusin.datumtesttask.domain.interactor.MapInteractor
import com.testfragment.vladdolgusin.datumtesttask.domain.repository.MapRepository

class MapPresentorImpl: MapPresentor, MapInteractor.CallbackLoadData {


    private var taskInteractorImpl: MapInteractor = MainModule.getCityIneractorImpl()
    private var mView: MapFragmentContract? = null

    override fun attachView(view: MapFragmentContract) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }

    override fun loadListCities(lat: Double, long: Double) {
        taskInteractorImpl.loadData(lat, long, this)
    }

    override fun onCompletedLoadData(cities: List<City>) {
        mView?.let {
            it.onLoadCities(cities)
        }
    }
}