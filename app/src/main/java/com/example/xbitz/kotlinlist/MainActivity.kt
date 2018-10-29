package com.example.xbitz.kotlinlist

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import com.example.xbitz.kotlinlist.adapter.ShoppingAdapter
import com.example.xbitz.kotlinlist.data.AppDatabase
import com.example.xbitz.kotlinlist.data.ShoppingItem
import com.example.xbitz.kotlinlist.touch.ShoppingTouchHelperAdapter
import com.example.xbitz.kotlinlist.touch.ShoppingTouchHelperCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ShoppingItemDialog.ShoppingItemHandler {
    companion object {
        val KEY_ITEM_TO_EDIT = "KEY_ITEM_TO_EDIT"
    }

    private lateinit var adapter: ShoppingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        fab.setOnClickListener {
            ShoppingItemDialog().show(supportFragmentManager, "TAG_ITEM")
        }

        initRecyclerView()

    }

    private fun initRecyclerView() {
        val dbThread = Thread {
            val items = AppDatabase.getInstance(this).shoppingItemDao().findAllItems()

            runOnUiThread {
                adapter = ShoppingAdapter(this, items)
                recyclerShopping.adapter = adapter

                val callback = ShoppingTouchHelperCallback(adapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerShopping)
            }
        }
        dbThread.start()
    }

    fun showEditDialog(itemToEdit: ShoppingItem) {
        val editDialog = ShoppingItemDialog()
        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_TO_EDIT, itemToEdit)
        editDialog.arguments = bundle
        editDialog.show(supportFragmentManager, "TAG_ITEM_EDIT")
    }

    override fun shoppingItemCreated(item: ShoppingItem) {
        val dbThread = Thread {
            val id = AppDatabase.getInstance(this@MainActivity).shoppingItemDao().insertItem(item)
            item.itemId = id
            runOnUiThread {
                adapter.addItem(item)
            }
        }
        dbThread.start()
    }

    override fun shoppingItemUpdated(item: ShoppingItem) {
        adapter.updateItem(item)

        val dbThread = Thread {
            AppDatabase.getInstance(this@MainActivity).shoppingItemDao().updateItem(item)

            runOnUiThread { adapter.updateItem(item) }
        }
        dbThread.start()
    }


}
