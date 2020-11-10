package com.example.restaurant.menu_items.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.restaurant.menu_items.data.MenuItem

@Dao
interface MenuItemDao {
    @Query("SELECT * from menu_items ORDER BY title ASC")
    fun getAll(): LiveData<List<MenuItem>>

    @Query("SELECT * FROM menu_items WHERE id=:id ")
    fun getById(id: String): LiveData<MenuItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: MenuItem)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: MenuItem)

    @Query("DELETE FROM menu_items")
    suspend fun deleteAll()
}