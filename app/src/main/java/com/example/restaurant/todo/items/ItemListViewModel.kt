package com.example.restaurant.todo.items

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurant.TAG
import com.example.restaurant.todo.data.ItemRepository
import com.example.restaurant.todo.data.MenuItem
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class ItemListViewModel : ViewModel() {
    private val mutableItems = MutableLiveData<List<MenuItem>>().apply { value = emptyList() }
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val items: LiveData<List<MenuItem>> = mutableItems
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    @RequiresApi(Build.VERSION_CODES.O)
    fun createItem(position: Int): Unit {
        val list = mutableListOf<MenuItem>()
        list.addAll(mutableItems.value!!)
        list.add(MenuItem(position, "Item " + position, "Description " + position, position.toFloat(), LocalDateTime.now().toString(), false))
        mutableItems.value = list
    }

    fun loadItems() {
        viewModelScope.launch {
            Log.v(TAG, "loadItems...");
            mutableLoading.value = true
            mutableException.value = null
            try {
                mutableItems.value = ItemRepository.loadAll()
                Log.d(TAG, "loadItems succeeded");
                mutableLoading.value = false
            } catch (e: Exception) {
                Log.w(TAG, "loadItems failed", e);
                mutableException.value = e
                mutableLoading.value = false
            }
        }
    }
}
