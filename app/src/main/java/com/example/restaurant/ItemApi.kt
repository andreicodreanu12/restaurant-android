package com.example.restaurant

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object ItemApi {
    private const val URL = "http://192.168.0.143:3000/"

    interface Service {
        @GET("/items")
        suspend fun find(): List<MenuItem>

        @GET("/item/{id}")
        suspend fun read(@Path("id") itemId: Int): MenuItem;

        @Headers("Content-Type: application/json")
        @POST("/item")
        suspend fun create(@Body item: MenuItem): MenuItem

        @Headers("Content-Type: application/json")
        @PUT("/item/{id}")
        suspend fun update(@Path("id") itemId: Int, @Body item: MenuItem): MenuItem
    }

    private val client: OkHttpClient = OkHttpClient.Builder().build()

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    val service: Service = retrofit.create(Service::class.java)
}