package com.example.xbitz.kotlinlist.touch

interface ShoppingTouchHelperAdapter {

    fun onItemDismissed(position: Int)

    fun onItemMoved(fromPosition: Int, toPosition: Int)
}
