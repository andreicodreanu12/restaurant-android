package com.example.restaurant.menu_items.item

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.restaurant.core.TAG
import com.example.restaurant.menu_items.data.MenuItemRepository
import com.example.restaurant.menu_items.data.MenuItem
import com.example.restaurant.core.Result
import com.example.restaurant.menu_items.data.local.MenuItemDatabase
import kotlinx.coroutines.launch


class MenuItemEditViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    val itemRepository: MenuItemRepository

    init {
        val itemDao = MenuItemDatabase.getDatabase(application, viewModelScope).itemDao()
        itemRepository = MenuItemRepository(itemDao)
    }

    fun getItemById(itemId: String): LiveData<MenuItem> {
        Log.v(TAG, "getItemById...")
        return itemRepository.getById(itemId)
    }

    fun saveOrUpdateItem(item: MenuItem) {
        viewModelScope.launch {
            Log.v(TAG, "saveOrUpdateItem...");
            mutableFetching.value = true
            mutableException.value = null
            val result: Result<MenuItem>
            if (item.id != -1) {
                result = itemRepository.update(item)
            } else {
                result = itemRepository.save(item)
            }
            when(result) {
                is Result.Success -> {
                    Log.d(TAG, "saveOrUpdateItem succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "saveOrUpdateItem failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }
}