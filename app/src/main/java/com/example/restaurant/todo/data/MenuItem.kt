package com.example.restaurant.todo.data

data class MenuItem(
    val id: Int,
    var title: String,
    var description: String,
    var price: Float,
    var introduced_at: String,
    var is_expensive: Boolean
) {
    override fun toString(): String {
        return super.toString()
    }
}