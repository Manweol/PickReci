package com.android.example.pickreci.ItemViews

import android.view.View
import android.view.View.OnTouchListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.android.example.pickreci.Models.Order
import com.android.example.pickreci.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder


class OrderItem(val order: Order): Item<ViewHolder>() {

    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var modeOfPayment: TextView
    private lateinit var price: TextView
    private lateinit var listView: ListView
    private lateinit var v: View




    override fun bind(viewHolder: ViewHolder, position: Int) {
        v = viewHolder.itemView
        init()
        initValues()




    }

    private fun initValues() {
        modeOfPayment.text = order.modeOfPayment
        price.text= "Php ${order.totalPrice}"
        //List view
        val listItems = order.orderDetails!!.split(".").toTypedArray()
        arrayAdapter = ArrayAdapter(v.context, android.R.layout.simple_list_item_1, listItems)
        listView.adapter = arrayAdapter

    }


    private fun init() {
        modeOfPayment = v.findViewById(R.id.textView16)
        price = v.findViewById(R.id.textView17)
        listView = v.findViewById(R.id.lv)
        listView.setOnTouchListener(OnTouchListener { v, event ->
            // Setting on Touch Listener for handling the touch inside ScrollView
            // Disallow the touch request for parent scroll on touch of child view
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        })
    }

    override fun getLayout(): Int {
        return R.layout.row_orders
    }
}