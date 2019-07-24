package com.chegsmania.currencyconverter.model

import com.google.gson.annotations.SerializedName

import io.realm.RealmObject

open class FixerRates : RealmObject(){
    @SerializedName("AED")
    var AED: String? = null

    @SerializedName("AUD")
    var AUD: String? = null

    @SerializedName("BTC")
    var BTC: String? = null

    @SerializedName("CAD")
    var CAD: String? = null

    @SerializedName("EUR")
    var EUR: String? = null

    @SerializedName("GBP")
    var GBP: String? = null

    @SerializedName("GHS")
    var GHS: String? = null

    @SerializedName("HKD")
    var HKD: String? = null

    @SerializedName("INR")
    var INR: String? = null

    @SerializedName("JPY")
    var JPY: String? = null

    @SerializedName("NGN")
    var NGN: String? = null

    @SerializedName("USD")
    var USD: String? = null

    @SerializedName("XAF")
    var XAF: String? = null

    @SerializedName("XAU")
    var XAU: String? = null

    @SerializedName("XPF")
    var XPF: String? = null

    @SerializedName("ZAR")
    var ZAR: String? = null

    @SerializedName("ZWL")
    var ZWL: String? = null
}