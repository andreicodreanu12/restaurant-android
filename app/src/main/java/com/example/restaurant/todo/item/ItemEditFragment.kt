package com.example.restaurant.todo.item

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restaurant.R
import com.example.restaurant.TAG
import kotlinx.android.synthetic.main.fragment_item_edit.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ItemEditFragment : Fragment() {
    companion object {
        const val ITEM_ID = "ITEM_ID"
    }

    private lateinit var viewModel: ItemEditViewModel
    private var itemId: String? = null

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
            val day: Int = item_introduced_at.dayOfMonth
            val month: Int = item_introduced_at.month + 1
            val year: Int = item_introduced_at.year
            val date = LocalDate.of(year,month,day)
            val time = LocalTime.now()
            val datetime = LocalDateTime.of(date, time)
            viewModel.saveOrUpdateItem(item_title.text.toString(), item_desc.text.toString(), item_price.text.toString().toFloat(), datetime.toString(), item_is_expensive.isChecked)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(ItemEditViewModel::class.java)
        viewModel.item.observe(viewLifecycleOwner, { item ->
            Log.v(TAG, "update items")
            item_title.setText(item.title)
            item_desc.setText(item.description)
            item_price.setText(item.price.toString())
        })
        viewModel.fetching.observe(viewLifecycleOwner, { fetching ->
            Log.v(TAG, "update fetching")
            progress.visibility = if (fetching) View.VISIBLE else View.GONE
        })
        viewModel.fetchingError.observe(viewLifecycleOwner, { exception ->
            if (exception != null) {
                Log.v(TAG, "update fetching error")
                val message = "Fetching exception ${exception.message}"
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.completed.observe(viewLifecycleOwner, Observer { completed ->
            if (completed) {
                Log.v(TAG, "completed, navigate back")
                findNavController().navigateUp()
            }
        })
        val id = itemId
        if (id != null) {
            viewModel.loadItem(id.toInt())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }
}
