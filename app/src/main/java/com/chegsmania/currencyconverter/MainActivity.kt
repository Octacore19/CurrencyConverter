package com.chegsmania.currencyconverter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.chegsmania.currencyconverter.manager.ConverterManager
import com.chegsmania.currencyconverter.model.FixerLatest
import com.chegsmania.currencyconverter.viewmodel.MainActivityViewModel
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val logTag: String = javaClass.simpleName
    private var foreignTextEditText: TextInputEditText? = null
    private var localTextEditText: TextInputEditText? = null
    private var model: MainActivityViewModel? = null
    private var foreignCurrentKey: String? = null
    private var localCurrencyKey: String? = null
    private var localLabel: TextView? = null
    private var foreignLabel: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = " "

        model = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        localTextEditText = findViewById(R.id.local_currency_edittext)
        foreignTextEditText = findViewById(R.id.foreign_currency_edittext)
        localLabel = findViewById(R.id.local_currency_label)
        foreignLabel = findViewById(R.id.foreign_currency_label)

        saveHistoryIntoRealm()
        Log.i(logTag, "Saved Rates into the database")
        initSpinners()
        convertToForeign()
    }

    private fun initSpinners() {
        val foreignCurrencySpinner: Spinner = findViewById(R.id.foreign_currency_spinner)
        val foreignAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.foreign_entries))
        foreignAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        foreignCurrencySpinner.adapter = foreignAdapter
        spinnerController(foreignCurrencySpinner)

        val localCurrencySpinner: Spinner = findViewById(R.id.local_currency_spinner)
        val localAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.local_entries))
        localAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        localCurrencySpinner.adapter = localAdapter
        spinnerController(localCurrencySpinner)
        localLabel?.text = model!!.base
    }

    private fun convertToForeign() {
        localTextEditText!!.setText(model?.local.toString())
        localTextEditText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                foreignTextEditText!!.setText(model?.convertToForeign().toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val local = s.toString()
                if (local.isEmpty() && count == 0) {
                    model?.local = 0
                    model?.foreign = 0.0
                } else {
                    model?.local = local.toInt()
                    try{
                        model?.foreign = model?.getAllRates()!![foreignCurrentKey]!!.toDouble()
                    } catch (err: Throwable){
                        Log.e(logTag, "Error loading from database, Database is empty", err)
                        Toast.makeText(applicationContext, "Database is empty, Please connect to the internet", Toast.LENGTH_LONG).show()
                    }

                }
            }

        })
    }

    private fun spinnerController(spinner: Spinner) {
        val id = spinner.id
        if (id == R.id.foreign_currency_spinner) {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(item: AdapterView<*>?, view: View?, position: Int, p3: Long) {
                    val currencyKey = item?.getItemAtPosition(position)
                    foreignCurrentKey = currencyKey.toString()
                    model?.setAllRates()
                    try {
                        model?.foreign = model?.getAllRates()!![currencyKey.toString()]!!.toDouble()
                    } catch (err: Throwable) {
                        Log.e(logTag, "Error loading from database, Database is empty", err)
                        Toast.makeText(applicationContext, "Database is empty, Please connect to the internet", Toast.LENGTH_LONG).show()
                    } finally {
                        val convert = model!!.convertToForeign()
                        foreignTextEditText!!.setText(convert.toString())

                        foreignLabel?.text = foreignCurrentKey
                        setSummaryExchangeRate()
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
        if(id == R.id.local_currency_spinner){
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(item: AdapterView<*>?, view: View?, position: Int, p3: Long) {
                    try {
                        val currencyKey = item?.getItemAtPosition(position)
                        model?.base = currencyKey.toString()
                        localCurrencyKey = currencyKey.toString()

                        model?.setAllRates()
                        model?.foreign = model?.getAllRates()!![foreignCurrentKey]!!.toDouble()

                        val convert = model!!.convertToForeign()
                        foreignTextEditText!!.setText(convert.toString())

                        localLabel?.text = model!!.base
                        setSummaryExchangeRate()

                    } catch (err: Exception) {

                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
        }
    }

    private fun setSummaryExchangeRate(){

        val exchangeInfo: TextView = findViewById(R.id.exchange_rate)
        val builder: StringBuilder = StringBuilder()
        builder.append("Mid-market exchange rate of ")
        builder.append(model!!.base)
        if (model!!.getAllRates()[foreignCurrentKey] == null){
            builder.append(" at 0 ")
        } else{
            builder.append(" at ${model!!.getAllRates()[foreignCurrentKey]} ")
        }
        builder.append(foreignCurrentKey)
        exchangeInfo.text = builder
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    private fun saveHistoryIntoRealm() {
        val converterManager = ConverterManager()
        val callResponse: Call<FixerLatest> = converterManager.latest("EUR")
        val progress: ProgressBar = findViewById(R.id.progressBar)
        progress.visibility = ProgressBar.VISIBLE
        progress.max = 100
        progress.progress = 50
        callResponse.enqueue(object : Callback<FixerLatest> {
            override fun onFailure(call: Call<FixerLatest>, t: Throwable) {
                progress.visibility = ProgressBar.GONE
                Log.d(logTag, "Error saving into database", t)
            }

            override fun onResponse(call: Call<FixerLatest>, response: Response<FixerLatest>) {
                val items = response.body()
                progress.progress = 75
                if (items != null) {
                    converterManager.addToHistory(items)
                }
                progress.visibility = ProgressBar.GONE
            }
        })
    }
}