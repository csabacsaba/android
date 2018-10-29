package com.example.xbitz.kotlinlist.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.xbitz.kotlinlist.MainActivity
import com.example.xbitz.kotlinlist.R
import com.example.xbitz.kotlinlist.data.ShoppingItem
import com.example.xbitz.kotlinlist.touch.ShoppingTouchHelperAdapter
import kotlinx.android.synthetic.main.row_item.view.*
import java.util.*

class ShoppingAdapter : RecyclerView.Adapter<ShoppingAdapter.ViewHolder>, ShoppingTouchHelperAdapter {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvName = itemView.tvName!!
        val tvPrice = itemView.tvPrice!!
        val cbBought = itemView.cbBought!!
        val btnDelete = itemView.btnDelete!!
        val btnEdit = itemView.btnEdit!!
    }

    private val items = mutableListOf<ShoppingItem>()
    private val context: Context

    constructor(context: Context, items: List<ShoppingItem>) : super() {
        this.context = context
        this.items.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.row_item, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onItemDismissed(position: Int) {
        deleteItem(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(items, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(items, i, i - 1)
            }
        }

        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = items[position].name
        holder.tvPrice.text = items[position].price.toString()
        holder.cbBought.isChecked = items[position].bought

        holder.btnEdit.setOnClickListener {
            (holder.itemView.context as MainActivity).showEditDialog(items[holder.adapterPosition])
        }

        holder.btnDelete.setOnClickListener {
            deleteItem(holder.adapterPosition)
        }
    }

    fun deleteItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addItem(item: ShoppingItem) {
        items.add(item)
        notifyItemInserted(items.lastIndex)
    }

    fun updateItem(item: ShoppingItem) {
        val idx = items.indexOf(item)
        notifyItemChanged(idx)
    }


}

