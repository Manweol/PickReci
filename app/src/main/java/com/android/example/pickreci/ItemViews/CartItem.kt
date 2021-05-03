package com.android.example.pickreci.ItemViews

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.example.pickreci.Models.CartProduct
import com.android.example.pickreci.Models.Product
import com.android.example.pickreci.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import org.w3c.dom.Text

class CartItem(val cartProduct: CartProduct): Item<ViewHolder>() {
    private lateinit var v: View
    private lateinit var image: ImageView
    private lateinit var productName: TextView
    private lateinit var productQuantity: TextView
    private lateinit var totalPrice: TextView
    private lateinit var actionButton: FloatingActionButton


    override fun bind(viewHolder: ViewHolder, position: Int) {
        v = viewHolder.itemView
        init()
        fetchProductData()
        initFloatingActionButton()

    }

    private fun initFloatingActionButton() {
        actionButton.setOnClickListener {
            removeFromCart(cartProduct)
        }


    }

    private fun removeFromCart(cartProduct: CartProduct) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser.uid!!
        val ref = FirebaseDatabase.getInstance().getReference("cart/$currentUserUid/${cartProduct.uid}")
        ref.removeValue().addOnSuccessListener {
            Toast.makeText(v.context, "Item removed from cart.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchProductData() {
        val ref = FirebaseDatabase.getInstance().getReference("/products/${cartProduct.productUid}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val product = snapshot.getValue(Product::class.java)!!
                Picasso.get().load(product.productImg).into(image)
                productName.text = product.productName
                productQuantity.text = cartProduct.quantity.toString()
                totalPrice.text = cartProduct.totalPrice.toString()


            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun init() {
        image = v.findViewById<ImageView>(R.id.imageView4)
        productName =  v. findViewById<TextView>(R.id.textView9)
        productQuantity = v.findViewById<TextView>(R.id.textView10)
        totalPrice = v.findViewById<TextView>(R.id.textView11)
        actionButton = v.findViewById(R.id.floatingActionButton2)
    }


    override fun getLayout(): Int {
        return R.layout.row_cart
    }
}