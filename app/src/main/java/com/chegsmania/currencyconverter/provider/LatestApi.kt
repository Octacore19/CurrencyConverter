package com.chegsmania.currencyconverter.provider

import com.chegsmania.currencyconverter.model.FixerLatest
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LatestApi {
    @GET("latest?access_key=044c16c13e6c4e39ee8d70735beae9b3")
    fun getLatestData(@Query("base") base: String) : Call<FixerLatest>
}