package com.testfragment.vladdolgusin.datumtesttask.data.entity

data class Cities (var mapCities: String, var limit: Int)

data class City (
    var id: Int,
    var name: String,
    var latitude: Double,
    var longitude: Double
)
