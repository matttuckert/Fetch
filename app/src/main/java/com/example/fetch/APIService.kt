package com.example.fetch

import retrofit2.Call
import retrofit2.http.GET

interface APIService {

    @GET("hiring.json")
    fun getData(): Call<ArrayList<ListItem>>
}