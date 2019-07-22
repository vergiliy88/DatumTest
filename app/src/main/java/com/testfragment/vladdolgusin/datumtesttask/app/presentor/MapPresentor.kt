package com.testfragment.vladdolgusin.datumtesttask.app.presentor

import com.testfragment.vladdolgusin.datumtesttask.app.fragment.map.MapFragmentContract

interface MapPresentor {
    fun attachView(view: MapFragmentContract)
    fun detachView()
    fun loadListCities(lat: Double, long: Double)
}