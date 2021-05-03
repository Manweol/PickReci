package com.android.example.pickreci

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pickreci.ItemViews.OrderItem
import com.android.example.pickreci.Models.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder


class Orders : Fragment() {

    //Rv
    private lateinit var recyclerView: RecyclerView
    private var adapter = GroupAdapter<ViewHolder>()




    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_orders, container, false)
        initRecyclerView()
        fetchOrders()

        return v
    }

    private fun fetchOrders() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser.uid!!
        val ref = FirebaseDatabase.getInstance().getReference("orders/$currentUserUid")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { order->
                    val data = order.getValue(Order::class.java)!!
                    adapter.add(OrderItem(data))
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    companion object {

    }

    private fun initRecyclerView() {
        recyclerView = v.findViewById(R.id.ordersRV)
        recyclerView.addItemDecoration(DividerItemDecoration(v.context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter


    }
}