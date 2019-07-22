package com.testfragment.vladdolgusin.datumtesttask.domain.interactor.impl


import com.testfragment.vladdolgusin.datumtesttask.app.data.City
import com.testfragment.vladdolgusin.datumtesttask.domain.interactor.MapInteractor
import com.testfragment.vladdolgusin.datumtesttask.domain.repository.MapRepository

class MapInteractorImpl(var repository: MapRepository): MapInteractor, MapRepository.CallbackLoadData {

    lateinit var callback: MapInteractor.CallbackLoadData

    override fun onCompletedLoadData(cities: List<City>) {
        callback.onCompletedLoadData(cities)
    }

    override fun loadData(lat: Double, long: Double, onCompletedLoadData: MapInteractor.CallbackLoadData) {
        callback = onCompletedLoadData
        repository.loadData(lat,long, this)
    }


}