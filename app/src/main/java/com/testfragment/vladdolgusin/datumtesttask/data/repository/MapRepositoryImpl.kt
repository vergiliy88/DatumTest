package com.testfragment.vladdolgusin.datumtesttask.data.repository


import android.util.Log
import com.google.gson.JsonParser
import com.testfragment.vladdolgusin.datumtesttask.app.data.City
import com.testfragment.vladdolgusin.datumtesttask.app.di.NetWork.NetworkApi
import com.testfragment.vladdolgusin.datumtesttask.domain.repository.MapRepository
import com.testfragment.vladdolgusin.datumtesttask.data.net.MapService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response


class MapRepositoryImpl() : MapRepository {

    var listCities: MutableList<City> = mutableListOf()

    override fun loadData(lat: Double, long: Double, onCompletedLoadData: MapRepository.CallbackLoadData) {

        NetworkApi.initialize()
        val messagesApi = NetworkApi.getService(MapService::class.java)


/*        fun testRX(): Observable<Int> {
            return Observable.create { subscibe ->
                for (item in 1..20) {
                    println("thread testRX ${Thread.currentThread().name}")
                    subscibe.onNext(item)
                }
            }
        }

        val dispose = testRX()

            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { item ->
                println("thread map ${Thread.currentThread().name}")
                item * 10
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                println("thread out ${Thread.currentThread().name}")
                println("$it")
            }, {

            }, {

            })*/

        messagesApi.getCurrentWeatherData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Response<ResponseBody>>() {
                override fun onNext(value: Response<ResponseBody>?) {
                    val json = value!!.body()!!.string()
                    val root =  JsonParser().parse(json)
                    for (i in 0..19) {
                        val city = root.asJsonObject.get(i.toString())
                        listCities.add(
                            City(
                                city.asJsonObject.get("id").asInt,
                                city.asJsonObject.get("name").asString,
                                city.asJsonObject.get("latitude").asDouble,
                                city.asJsonObject.get("longitude").asDouble,
                                0.0)
                        )
                    }
                    onCompletedLoadData.onCompletedLoadData(listCities)
                }

                override fun onComplete() {

                }

                override fun onError(e: Throwable) {
                    Log.i("MAP", e.toString())
                }
            })
    }
}