package com.chegsmania.currencyconverter.viewmodel

import androidx.lifecycle.ViewModel
import com.chegsmania.currencyconverter.manager.ConverterManager

class MainActivityViewModel : ViewModel() {
    private val converterManager: ConverterManager = ConverterManager()
    private var allRates : HashMap<String, String> = HashMap()
    var base: String = "EUR"
    var local: Int = 0
    var foreign: Double = 0.0

    fun setAllRates(){
        val rates = converterManager.getAllRates(base)?.rates
        if (rates != null){
            allRates["AED"] = rates.AED.toString()
            allRates["AUD"] = rates.AUD.toString()
            allRates["BTC"] = rates.BTC.toString()
            allRates["CAD"] = rates.CAD.toString()
            allRates["EUR"] = rates.EUR.toString()
            allRates["GBP"] = rates.GBP.toString()
            allRates["GHS"] = rates.GHS.toString()
            allRates["HKD"] = rates.HKD.toString()
            allRates["INR"] = rates.INR.toString()
            allRates["JPY"] = rates.JPY.toString()
            allRates["NGN"] = rates.NGN.toString()
            allRates["USD"] = rates.USD.toString()
            allRates["XAF"] = rates.XAF.toString()
            allRates["XAU"] = rates.XAU.toString()
            allRates["XPF"] = rates.XPF.toString()
            allRates["ZAR"] = rates.ZAR.toString()
            allRates["ZWL"] = rates.ZWL.toString()
        }
    }

    fun getAllRates() : HashMap<String, String>{
        return allRates
    }

    fun convertToForeign() : Double{
        return (local * foreign)
    }

    fun convertToLocal() : Double{
        return (local / foreign)
    }
}