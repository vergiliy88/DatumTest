package com.testfragment.vladdolgusin.datumtesttask.domain.repository

import com.testfragment.vladdolgusin.datumtesttask.app.data.City


interface MapRepository {
    interface CallbackLoadData {
        fun onCompletedLoadData(cities: List<City>)
    }

    fun loadData(lat: Double, long: Double, onCompletedLoadData: CallbackLoadData)

}