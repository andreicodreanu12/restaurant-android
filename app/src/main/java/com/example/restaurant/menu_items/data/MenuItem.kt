package com.example.restaurant.menu_items.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName= "menu_items")
data class MenuItem(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name= "title") var title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "price") var price: Float,
    @ColumnInfo(name = "introduce_at") var introduced_at: String,
    @ColumnInfo(name = "is_expensive") var is_expensive: Boolean
) {
    override fun toString(): String {
        return super.toString()
    }
}