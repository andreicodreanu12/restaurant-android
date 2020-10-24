package com.example.restaurant.todo.data

import android.util.Log
import com.example.restaurant.TAG
import com.example.restaurant.todo.data.remote.ItemApi
import com.example.restaurant.core.Result

object ItemRepository {
    private var cachedItems: MutableList<MenuItem>? = null;

    suspend fun loadAll(): List<MenuItem> {
        Log.i(TAG, "loadAll")
        if (cachedItems != null) {
            return cachedItems as List<MenuItem>;
        }
        cachedItems = mutableListOf()
        val items = ItemApi.service.find()
        cachedItems?.addAll(items)
        return cachedItems as List<MenuItem>
    }

    suspend fun load(itemId: Int): MenuItem {
        Log.i(TAG, "load")
        val item = cachedItems?.find { it.id == itemId }
        if (item != null) {
            return item
        }
        return ItemApi.service.read(itemId)
    }

    suspend fun save(item: MenuItem): Result<MenuItem> {
        try {
            val createdItem = ItemApi.service.create(item)
            cachedItems?.add(createdItem)
            return Result.Success(createdItem)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun update(item: MenuItem): Result<MenuItem> {
        try {
            val updatedItem = ItemApi.service.update(item.id, item)
            val index = cachedItems?.indexOfFirst { it.id == item.id }
            if (index != null) {
                cachedItems?.set(index, updatedItem)
            }
            return Result.Success(updatedItem)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }
}