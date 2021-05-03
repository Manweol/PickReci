package com.android.example.pickreci.Cart

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pickreci.InitialScreen.InitialScreen
import com.android.example.pickreci.ItemViews.CartItem
import com.android.example.pickreci.Models.CartProduct
import com.android.example.pickreci.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class CartActivity : Fragment() {

    private lateinit var v: View
    //Rv
    private lateinit var recyclerView: RecyclerView
    private var adapter = GroupAdapter<ViewHolder>()
    //Button
    private lateinit var button: Button
    //TEXT vIEW
    private lateinit var totalPrice: TextView


    companion object {
        const val TAG = "CartTag"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_cart_activity, container, false)
        initRecyclerView()
        init()
        initButton()
        fetchCartProduct()



        return v
    }

    private fun initButton() {
        button.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val dialogBuilder = AlertDialog.Builder(v.context)
        dialogBuilder.setMessage("The only mode of payment avaialable right now is Cash on Delivery(COD). Do you still want to check out?")
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                showDialogAgain()
            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Checkout")
        alert.show()

    }

    private fun showDialogAgain() {
        val dialogBuilder = AlertDialog.Builder(v.context)
        dialogBuilder.setMessage("The order details will be set according to your personal data. Please make sure that your personal information are correct.")
            .setCancelable(false)
            .setPositiveButton("Checkout", DialogInterface.OnClickListener { _, _ ->
                updateOrder()
            })
            .setNegativeButton("Edit Details", DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Checkout")
        alert.show()
    }

    private fun updateOrder() {

    }

    private fun fetchCartProduct() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser.uid!!
        val ref = FirebaseDatabase.getInstance().getReference("cart/$currentUserUid")
        ref.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                var total: Double = 0.0
                adapter.clear()
                snapshot.children.forEach { cartProduct ->
                    val data = cartProduct.getValue(CartProduct::class.java)!!
                    adapter.add(CartItem(data))
                    total += data.totalPrice!!
                }
                totalPrice.text = "Total: Php $total"

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun init() {
        button = v.findViewById(R.id.checkOutButton)
        totalPrice = v.findViewById(R.id.textView12)
    }


    private fun initRecyclerView() {
        recyclerView = v.findViewById(R.id.cartRv)
//        recyclerView.addItemDecoration(DividerItemDecoration(v.context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter


    }

}