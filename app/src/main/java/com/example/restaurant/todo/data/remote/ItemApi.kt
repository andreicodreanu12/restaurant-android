package com.example.restaurant.todo.data.remote

import com.example.restaurant.core.Api
import com.example.restaurant.todo.data.MenuItem
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object ItemApi {

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

    val service: Service = Api.retrofit.create(Service::class.java)
}