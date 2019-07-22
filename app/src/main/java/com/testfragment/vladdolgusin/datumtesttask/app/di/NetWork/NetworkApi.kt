package com.testfragment.vladdolgusin.datumtesttask.app.di.NetWork

import android.app.Application
import io.reactivex.android.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkApi {
    private const val BASE_URL = "https://htmlweb.ru/"
    private lateinit var retrofit: Retrofit


    fun initialize() {
        retrofit = getRetrofit(getOkHttpClient())
    }

    fun <T> getService(className: Class<T>): T = retrofit.create(className)

    private fun getRetrofit(client: OkHttpClient) =
        Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    private fun getOkHttpClient() =
        OkHttpClient().newBuilder()
            .readTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .build()

}