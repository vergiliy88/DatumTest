package com.testfragment.vladdolgusin.datumtesttask.data.net


import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface MapService {
    @GET("/geo/api.php?city_coming&latitude=47.229736&longitude=39.744284&json&perpage=20")
    fun getCurrentWeatherData( ): Observable<Response<ResponseBody>>
}