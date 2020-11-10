package com.example.restaurant.menu_items.items

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.restaurant.core.TAG
import com.example.restaurant.menu_items.data.MenuItemRepository
import com.example.restaurant.menu_items.data.MenuItem
import com.example.restaurant.core.Result
import com.example.restaurant.menu_items.data.local.MenuItemDatabase

import kotlinx.coroutines.launch


class MenuItemListViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val items: LiveData<List<MenuItem>>
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    val itemRepository: MenuItemRepository

    init {
        val itemDao = MenuItemDatabase.getDatabase(application, viewModelScope).itemDao()
        itemRepository = MenuItemRepository(itemDao)
        items = itemRepository.items
    }

    fun refresh() {
        viewModelScope.launch {
            Log.v(TAG, "refresh...");
            mutableLoading.value = true
            mutableException.value = null
            when (val result = itemRepository.refresh()) {
                is Result.Success -> {
                    Log.d(TAG, "refresh succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "refresh failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableLoading.value = false
        }
    }
}
