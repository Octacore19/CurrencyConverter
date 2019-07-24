package com.chegsmania.currencyconverter.provider

import com.chegsmania.currencyconverter.model.FixerLatest
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConverterDataProvider {

    private val BaseUrl: String = "http://data.fixer.io/api/"
    private val latestApi: LatestApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        latestApi = retrofit.create(LatestApi::class.java)
    }

    fun getLatest(base: String) : Call<FixerLatest>{
        return latestApi.getLatestData(base)
    }
}