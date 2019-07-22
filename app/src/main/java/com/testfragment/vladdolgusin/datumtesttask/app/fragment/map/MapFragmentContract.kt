package com.testfragment.vladdolgusin.datumtesttask.app.fragment.map

import com.testfragment.vladdolgusin.datumtesttask.app.data.City

interface MapFragmentContract {
    fun onLoadCities(cities: List<City>)
    fun onError()
}