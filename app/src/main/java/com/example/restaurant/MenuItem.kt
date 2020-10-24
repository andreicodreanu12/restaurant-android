package com.example.restaurant

data class MenuItem(
    val id: Int,
    val title: String,
    val description: String,
    val price: Float
) {
    override fun toString(): String {
        return super.toString()
    }
}