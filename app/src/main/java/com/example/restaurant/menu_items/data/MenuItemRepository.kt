package com.example.restaurant.menu_items.data

import androidx.lifecycle.LiveData
import com.example.restaurant.menu_items.data.remote.MenuItemApi
import com.example.restaurant.core.Result
import com.example.restaurant.menu_items.data.local.MenuItemDao

class MenuItemRepository(private val itemDao: MenuItemDao) {

    val items = itemDao.getAll()

    suspend fun refresh(): Result<Boolean> {
        try {
            val items = MenuItemApi.service.find()
            for (item in items) {
                itemDao.insert(item)
            }
            return Result.Success(true)
        } catch(e: Exception) {
            return Result.Error(e)
        }
    }

    fun getById(itemId: String): LiveData<MenuItem> {
        return itemDao.getById(itemId)
    }

    suspend fun save(item: MenuItem): Result<MenuItem> {
        try {
            val createdItem = MenuItemApi.service.create(item)
            itemDao.insert(createdItem)
            return Result.Success(createdItem)
        } catch(e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun update(item: MenuItem): Result<MenuItem> {
        try {
            val updatedItem = MenuItemApi.service.update(item.id, item)
            itemDao.update(updatedItem)
            return Result.Success(updatedItem)
        } catch(e: Exception) {
            return Result.Error(e)
        }
    }
}