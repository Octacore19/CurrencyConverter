package com.chegsmania.currencyconverter.database

import android.app.Application
import com.chegsmania.currencyconverter.manager.ConverterManager
import com.chegsmania.currencyconverter.model.FixerLatest
import com.chegsmania.currencyconverter.provider.ConverterDataProvider
import io.realm.Realm
import io.realm.RealmConfiguration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config: RealmConfiguration = RealmConfiguration.Builder()
            .name("history.realm")
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
    }
}