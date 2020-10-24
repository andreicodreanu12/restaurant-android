package com.example.restaurant

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_item.view.*

class ItemListAdapter(
    private val fragment: Fragment,
) : RecyclerView.Adapter<ItemListAdapter.ViewHolder>() {

    var items = emptyList<MenuItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var onItemClick: View.OnClickListener;

    init {
        onItemClick = View.OnClickListener { view ->
            val item = view.tag as MenuItem
            fragment.findNavController().navigate(R.id.ItemEditFragment, Bundle().apply {
                putString(ItemEditFragment.ITEM_ID, item.id.toString())
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item, parent, false)
        Log.v(TAG, "onCreateViewHolder")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder $position")
        val item = items[position]
        holder.titleView.text = item.title
        holder.descView.text = item.description
        holder.priceView.text = item.price.toString()
        holder.itemView.tag = item
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.title
        val descView: TextView = view.desc
        val priceView: TextView = view.price
    }
}