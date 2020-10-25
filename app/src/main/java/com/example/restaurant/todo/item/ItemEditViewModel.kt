package com.example.restaurant.todo.item

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
import com.example.restaurant.core.Result
import kotlinx.coroutines.launch
import java.sql.Date
import java.time.LocalDate
import java.time.LocalDateTime


class ItemEditViewModel : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    private val mutableItem = MutableLiveData<MenuItem>().apply { value = MenuItem(0, "", "", 0.0f, LocalDateTime.now().toString(), false) }
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    @RequiresApi(Build.VERSION_CODES.O)
    val item: LiveData<MenuItem> = mutableItem
    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadItem(itemId: Int) {
        viewModelScope.launch {
            Log.i(TAG, "loadItem...")
            mutableFetching.value = true
            mutableException.value = null
            try {
                mutableItem.value = ItemRepository.load(itemId)
                Log.i(TAG, "loadItem succeeded")
                mutableFetching.value = false
            } catch (e: Exception) {
                Log.w(TAG, "loadItem failed", e)
                mutableException.value = e
                mutableFetching.value = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveOrUpdateItem(title: String, description: String, price: Float, introduced_at: String, is_expensive: Boolean) {
        viewModelScope.launch {
            Log.i(TAG, "saveOrUpdateItem...");
            val item = mutableItem.value ?: return@launch
            item.title = title
            item.description = description
            item.price = price
            item.introduced_at = introduced_at
            item.is_expensive = is_expensive

            mutableFetching.value = true
            mutableException.value = null
            val result: Result<MenuItem>
            if (item.id != 0) {
                result = ItemRepository.update(item)
            } else {
                result = ItemRepository.save(item)
            }
            when (result) {
                is Result.Success -> {
                    Log.d(TAG, "saveOrUpdateItem succeeded");
                    mutableItem.value = result.data
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