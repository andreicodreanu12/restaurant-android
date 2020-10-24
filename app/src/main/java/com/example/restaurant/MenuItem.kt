package com.example.restaurant

data class MenuItem(
    val id: Int,
    var title: String,
    var description: String,
    var price: Float
) {
    override fun toString(): String {
        return super.toString()
    }
}