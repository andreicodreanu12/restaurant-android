package com.example.restaurant.menu_items.items

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restaurant.R
import com.example.restaurant.auth.data.AuthRepository
import com.example.restaurant.core.Constants
import com.example.restaurant.core.TAG
import kotlinx.android.synthetic.main.fragment_item_list.*

class MenuItemListFragment : Fragment() {
    private lateinit var menuItemListAdapter: MenuItemListAdapter
    private lateinit var itemsModelMenu: MenuItemListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        if (Constants.instance()?.fetchValueString("token") == null) {
            findNavController().navigate(R.id.fragment_login)
            return;
        }
        setupItemList()
        fab.setOnClickListener {
            Log.v(TAG, "add new item")
            findNavController().navigate(R.id.ItemEditFragment)
        }
        logout.setOnClickListener{
            Log.v(TAG, "LOGOUT")
            AuthRepository.logout()
            findNavController().navigate(R.id.fragment_login)
        }
    }

    private fun setupItemList() {
        menuItemListAdapter = MenuItemListAdapter(this)
        item_list.adapter = menuItemListAdapter
        itemsModelMenu = ViewModelProvider(this).get(MenuItemListViewModel::class.java)
        itemsModelMenu.items.observe(viewLifecycleOwner, { items ->
            Log.v(TAG, "update items")
            menuItemListAdapter.items = items
        })
        itemsModelMenu.loading.observe(viewLifecycleOwner, { loading ->
            Log.i(TAG, "update loading")
            progress.visibility = if (loading) View.VISIBLE else View.GONE
        })
        itemsModelMenu.loadingError.observe(viewLifecycleOwner, { exception ->
            if (exception != null) {
                Log.i(TAG, "update loading error")
                val message = "Loading exception ${exception.message}"
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        })
        itemsModelMenu.refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy")
    }
}