package com.example.restaurant

import android.util.Log

object ItemRepository {
    suspend fun getAll(): List<MenuItem> {
        Log.i(TAG, "getAll");
        return ItemApi.service.getAll()
    }
}