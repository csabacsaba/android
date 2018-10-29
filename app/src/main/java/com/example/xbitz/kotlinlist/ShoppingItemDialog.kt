package com.example.xbitz.kotlinlist

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.EditText
import com.example.xbitz.kotlinlist.data.ShoppingItem
import kotlinx.android.synthetic.main.dialog_create_item.view.*

class ShoppingItemDialog : DialogFragment() {

    private lateinit var shoppingItemHandler: ShoppingItemHandler

    private lateinit var etItem: EditText
    private lateinit var etPrice: EditText

    interface ShoppingItemHandler {
        fun shoppingItemCreated(item: ShoppingItem)

        fun shoppingItemUpdated(item: ShoppingItem)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is ShoppingItemHandler) {
            shoppingItemHandler = context
        } else {
            throw RuntimeException("The Activity does not implement the ShoppingItemHandler interface")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("New Item")
        initDialogContent(builder)
        builder.setPositiveButton("OK") { dialog, which ->
            // keep it empty
        }
        return builder.create()
    }

    private fun initDialogContent(builder: AlertDialog.Builder) {
        /*etItem = EditText(activity)
        builder.setView(etItem)*/

        val rootView = requireActivity().layoutInflater.inflate(R.layout.dialog_create_item, null)
        etItem = rootView.etName
        etPrice = rootView.etPrice
        builder.setView(rootView)

        val arguments = this.arguments
        if (arguments != null && arguments.containsKey(MainActivity.KEY_ITEM_TO_EDIT)) {
            val item = arguments.getSerializable(
                MainActivity.KEY_ITEM_TO_EDIT) as ShoppingItem
            etItem.setText(item.name)
            etPrice.setText(item.price.toString())

            builder.setTitle("Edit todo")
        }
    }


    override fun onResume() {
        super.onResume()

        val dialog = dialog as AlertDialog
        val positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)

        positiveButton.setOnClickListener {
            if (etItem.text.isNotEmpty()) {
                val arguments = this.arguments
                if (arguments != null && arguments.containsKey(MainActivity.KEY_ITEM_TO_EDIT)) {
                    handleItemEdit()
                } else {
                    handleItemCreate()
                }

                dialog.dismiss()
            } else {
                etItem.error = "This field can not be empty"
            }
        }
    }

    private fun handleItemCreate() {
        shoppingItemHandler.shoppingItemCreated(ShoppingItem(
            null,
            etItem.text.toString(),
            etPrice.text.toString().toInt(),
            false
        ))
    }

    private fun handleItemEdit() {
        val itemToEdit = arguments?.getSerializable(
            MainActivity.KEY_ITEM_TO_EDIT) as ShoppingItem
        itemToEdit.name = etItem.text.toString()
        itemToEdit.price = etPrice.text.toString().toInt()


        shoppingItemHandler.shoppingItemUpdated(itemToEdit)
    }
}