package com.chegsmania.currencyconverter.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class FixerLatest : RealmObject(){
    @SerializedName("timestamp")
    var timestamp: Long? = null

    @SerializedName("base")
    var base: String? = null

    @PrimaryKey
    @SerializedName("date")
    var date: String? = null

    @SerializedName("rates")
    var rates: FixerRates? = null
}