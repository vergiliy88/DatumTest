package com.testfragment.vladdolgusin.datumtesttask.domain.interactor

import com.testfragment.vladdolgusin.datumtesttask.app.data.City

interface MapInteractor {

    interface CallbackLoadData {
         fun onCompletedLoadData(cities: List<City>)
    }

    fun loadData(lat: Double, long: Double, onCompletedLoadData: CallbackLoadData)

}