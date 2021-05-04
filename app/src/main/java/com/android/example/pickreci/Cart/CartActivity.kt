package com.android.example.pickreci.Cart

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pickreci.Constants
import com.android.example.pickreci.ItemViews.CartItem
import com.android.example.pickreci.Models.CartProduct
import com.android.example.pickreci.Models.Order
import com.android.example.pickreci.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlin.math.log

class CartActivity : Fragment() {

    private lateinit var v: View

    //Rv
    private lateinit var recyclerView: RecyclerView
    private var adapter = GroupAdapter<ViewHolder>()

    //Button
    private lateinit var button: Button

    //TEXT vIEW
    private lateinit var totalPrice: TextView
    private lateinit var detailsTextView: TextView
    private lateinit var signTV:TextView
    private lateinit var progressBar : ProgressBar


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
        dialogBuilder.setMessage("The only mode of payment available right now is Cash on Delivery(COD). Do you still want to create order?")
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                showDialogAgain()
            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Create order")
        alert.show()

    }

    private fun showDialogAgain() {
        val dialogBuilder = AlertDialog.Builder(v.context)
        dialogBuilder.setMessage("The order details will be set according to your personal data. Please make sure that your personal information are correct.")
            .setCancelable(false)
            .setPositiveButton("Continue", DialogInterface.OnClickListener { _, _ ->
                createOrder()
            })
            .setNegativeButton("Edit Details", DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Create order")
        alert.show()
    }

    private fun createOrder() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser.uid!!
        val ref = FirebaseDatabase.getInstance().getReference("/orders/$currentUserUid")
        val key = ref.push().key!!

        val order = Order(
            uid = key,
            orderedBy = currentUserUid,
            orderDetails = detailsTextView.text.toString(),
            totalPrice = totalPrice.text.toString().toDouble(),
            modeOfPayment = Constants.COD,
        )
        ref.child(key).setValue(order).addOnSuccessListener {
            Toast.makeText(v.context, "Order Created", Toast.LENGTH_SHORT).show()
            _deleteCart(currentUserUid)
        }
            .addOnFailureListener { exception ->
                Toast.makeText(v.context, "${exception.message}", Toast.LENGTH_SHORT).show()
            }


    }

    private fun _deleteCart(currentUserUid: String) {
        val ref = FirebaseDatabase.getInstance().getReference("cart/$currentUserUid")
        ref.removeValue()

    }

    fun fetchCartProduct() {
        progressBar.isVisible = true
        val currentUserUid = FirebaseAuth.getInstance().currentUser.uid!!
        val ref = FirebaseDatabase.getInstance().getReference("cart/$currentUserUid")



        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var total: Double = 0.0
                var details: String? = ""
                adapter.clear()
                var countList: ArrayList<Int> = ArrayList()

                snapshot.children.forEachIndexed() { index, cartProduct ->
                    val data = cartProduct.getValue(CartProduct::class.java)!!
                    adapter.add(CartItem(data))
                    total += data.totalPrice!!
                    details += "[${index + 1 }] ${data.quantity} pc(s) of ${data.name}."


                }
                progressBar.isVisible = false
                signTV.isVisible = adapter.itemCount == 0
                detailsTextView.text = details
                totalPrice.text = "$total"

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun init() {
        signTV = v.findViewById(R.id.textView15)
        signTV.isVisible = false
        progressBar = v.findViewById(R.id.progressBar2)
        progressBar.isVisible = false
        button = v.findViewById(R.id.checkOutButton)
        totalPrice = v.findViewById(R.id.textView12)
        detailsTextView = v.findViewById(R.id.textView13)
    }


    private fun initRecyclerView() {
        recyclerView = v.findViewById(R.id.cartRv)
//        recyclerView.addItemDecoration(DividerItemDecoration(v.context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter


    }

}