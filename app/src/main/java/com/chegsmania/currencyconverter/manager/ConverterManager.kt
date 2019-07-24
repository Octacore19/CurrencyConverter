package com.chegsmania.currencyconverter.manager

import android.os.Build
import com.chegsmania.currencyconverter.database.LatestRepository
import com.chegsmania.currencyconverter.model.FixerLatest
import com.chegsmania.currencyconverter.provider.ConverterDataProvider
import retrofit2.Call
import java.time.LocalDate

class ConverterManager(
    private val provider: ConverterDataProvider = ConverterDataProvider(),
    private val latestRepo: LatestRepository = LatestRepository()) {

    fun latest(base: String): Call<FixerLatest> {
        return provider.getLatest(base)
    }

    fun addToHistory(latest: FixerLatest){
        latestRepo.addHistory(latest)
    }

    fun getAllRates(base: String) : FixerLatest? {
        val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        return latestRepo.getLatestData(base, date.toString())
    }
}