package com.example.fetch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val baseUrl = "https://fetch-hiring.s3.amazonaws.com/"
    private var names = hashMapOf<String, MutableList<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getData()
    }

    private fun getData() {
        val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(APIService::class.java)
        val call = service.getData()

        call.enqueue(object: Callback<ArrayList<ListItem>> {
            override fun onResponse(call: Call<ArrayList<ListItem>>, response: Response<ArrayList<ListItem>>) {
                if (response.code() == 200 && response.body() != null) {
                    setupData(response.body())
                }
            }
            override fun onFailure(call: Call<ArrayList<ListItem>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun setupData(listItems: ArrayList<ListItem>) {
        val listIds = hashSetOf<String>()

        for (item in listItems) {
            if (item.name != null && item.name.isNotEmpty()) {
                listIds.add(item.listId)
                if (!names.containsKey(item.listId)) {
                    names[item.listId] = mutableListOf()
                }
                names[item.listId]?.add(item.name)
            }
        }
        names.forEach { (_, value) -> value.sort() }
        initializeSpinner(listIds.toList())
    }

    private fun initializeSpinner(listIds: List<String>) {

        val spinner: Spinner = findViewById(R.id.list_id)
        ArrayAdapter(this, android.R.layout.simple_spinner_item, listIds).also { adapter ->
            adapter.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item))
            spinner.adapter = adapter
            spinner.onItemSelectedListener = this
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        val listView: ListView = findViewById(R.id.list_view)
        val adapter = names[parent.getItemAtPosition(pos)]?.let { ArrayAdapter(this, android.R.layout.simple_list_item_1, it.toList()) }
        listView.adapter = adapter
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}