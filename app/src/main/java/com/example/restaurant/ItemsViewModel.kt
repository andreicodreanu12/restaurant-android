package com.example.restaurant

import java.util.ArrayList

object ItemsViewModel {
    val items: MutableList<MenuItem> = ArrayList()

    private val COUNT = 100

    init {
        for (i in 1..COUNT) {
            addItem(createItem(i))
        }
    }

    private fun addItem(item: MenuItem) {
        items.add(item)
    }

    private fun createItem(position: Int): MenuItem {
        return MenuItem(position,  "Item " + position, "description " + position, (position).toFloat())
    }

}