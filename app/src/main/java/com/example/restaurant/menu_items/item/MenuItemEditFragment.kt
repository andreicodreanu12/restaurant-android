package com.example.restaurant.menu_items.item

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restaurant.R
import com.example.restaurant.core.TAG
import com.example.restaurant.menu_items.data.MenuItem
import kotlinx.android.synthetic.main.fragment_item_edit.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MenuItemEditFragment : Fragment() {
    companion object {
        const val ITEM_ID = "ITEM_ID"
    }

    private lateinit var viewModelMenu: MenuItemEditViewModel
    private var itemId: String? = null
    private var item: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if(it.containsKey(ITEM_ID)) {
                itemId = it.getString(ITEM_ID).toString()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        setupViewModel()
        fab.setOnClickListener {
            Log.v(TAG, "save item")
            val i = item
            val day: Int = item_introduced_at.dayOfMonth
            val month: Int = item_introduced_at.month + 1
            val year: Int = item_introduced_at.year
            val date = LocalDate.of(year,month,day)
            val time = LocalTime.now()
            val datetime = LocalDateTime.of(date, time)
            if (i != null) {
                i.title = item_title.text.toString()
                i.description = item_desc.text.toString()
                i.price = item_price.text.toString().toFloat()
                i.introduced_at = datetime.toString()
                i.is_expensive = item_is_expensive.isChecked
                viewModelMenu.saveOrUpdateItem(i)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupViewModel() {
        viewModelMenu = ViewModelProvider(this).get(MenuItemEditViewModel::class.java)
        viewModelMenu.fetching.observe(viewLifecycleOwner, { fetching ->
            Log.v(TAG, "update fetching")
            progress.visibility = if (fetching) View.VISIBLE else View.GONE
        })
        viewModelMenu.fetchingError.observe(viewLifecycleOwner, { exception ->
            if (exception != null) {
                Log.v(TAG, "update fetching error")
                val message = "Fetching exception ${exception.message}"
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModelMenu.completed.observe(viewLifecycleOwner, { completed ->
            if (completed) {
                Log.v(TAG, "completed, navigate back")
                findNavController().popBackStack()
            }
        })
        val id = itemId
        if (id == null) {
            item = MenuItem(-1, "", "", 0.toFloat(), LocalDateTime.now().toString(), false);
        } else {
            viewModelMenu.getItemById(id).observe(viewLifecycleOwner, {
                Log.v(TAG, "update items")
                if (it != null) {
                    item = it
                    item_title.setText(it.title)
                    item_desc.setText(it.description)
                    item_price.setText(it.price.toString())
                    Log.v(TAG, item!!.introduced_at)
                    item_is_expensive.isChecked = it.is_expensive;
                }
            })
        }
    }
}
