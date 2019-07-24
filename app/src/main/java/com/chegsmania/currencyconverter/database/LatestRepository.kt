package com.chegsmania.currencyconverter.database

import com.chegsmania.currencyconverter.model.FixerLatest
import io.realm.Realm

class LatestRepository(private val realm: Realm? = Realm.getDefaultInstance()) {

    fun addHistory(latest: FixerLatest) {
        realm?.executeTransaction {
            val fixerLatest = FixerLatest()
            fixerLatest.timestamp = latest.timestamp
            fixerLatest.base = latest.base
            fixerLatest.date = latest.date
            fixerLatest.rates = latest.rates
            realm.insertOrUpdate(fixerLatest)
        }
    }

    fun getLatestData(base: String, date: String): FixerLatest? {
        return realm?.where(FixerLatest::class.java)
            ?.equalTo("date", date)
            ?.and()
            ?.equalTo("base", base)
            ?.findFirst()
    }
}