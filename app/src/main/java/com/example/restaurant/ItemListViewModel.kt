package com.example.restaurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ItemListViewModel : ViewModel() {
    private val mutableItems = MutableLiveData<List<MenuItem>>().apply { value = emptyList() }

    val items: LiveData<List<MenuItem>> = mutableItems

    init {
        viewModelScope.launch {
            while(true) {
                val index = simulateNewItemNotification()
                createItem(index)
        }
    }
}

    fun createItem(position: Int): Unit {
        val list = mutableListOf<MenuItem>()
        list.addAll(mutableItems.value!!)
        list.add(MenuItem(position, "Item " + position, "Description " + position, position.toFloat()))
        mutableItems.value = list
    }

    var index = 100;
    suspend fun simulateNewItemNotification(): Int = withContext(Dispatchers.Default) {
        delay(1000);
        return@withContext ++index;
    }
}
