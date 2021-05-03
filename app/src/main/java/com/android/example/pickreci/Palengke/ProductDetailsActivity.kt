package com.android.example.pickreci.Palengke

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.example.pickreci.Cart.CartActivity
import com.android.example.pickreci.Models.Product
import com.android.example.pickreci.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.android.example.pickreci.Models.CartProduct as CartProduct1

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var addToCartActivity: CartActivity
    private lateinit var productImageView: ImageView
    private lateinit var producNameTextView: TextView
    private lateinit var productWeightTextView: TextView
    private lateinit var quantityTextView: TextView
    private lateinit var quantitySeekBar: SeekBar
    private lateinit var priceTextView: TextView
    private lateinit var button: Button
    var totalPrice: Double = 0.0
    var totalQuantity: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        val product = intent.getParcelableExtra<Product>(Palengke.TAG)!!
        init()
        initValues(product)
        initSeekBar(product)
        initAddToCartButton(product)

    }

    private fun initAddToCartButton(product: Product) {
        button.setOnClickListener {
            if (totalQuantity > 0) {
                //Add the product to cart
                addToCart(product)
            } else {
                Toast.makeText(this, "Quantity must not be 0", Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun addToCart(product: Product) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser.uid
        val ref = FirebaseDatabase.getInstance().getReference("cart/$currentUserUid")
        val key = ref.push().key!!

        //save this cartProduct
        val cartProduct = CartProduct1(
            uid = key,
            productUid = product.uid,
            quantity = totalQuantity,
            totalPrice =totalPrice,
            name = product.productName
        )
        ref.child(key).setValue(cartProduct).addOnSuccessListener {
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show()
            finish()
        }



    }

    private fun initSeekBar(product: Product) {
        quantitySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                totalQuantity = progress
                totalPrice = (product.price)!! * totalQuantity
                priceTextView.text = "Php $totalPrice"
                quantityTextView.text = "$totalQuantity"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                totalQuantity= seekBar!!.progress
                totalPrice = (product.price)!! * totalQuantity
                priceTextView.text = "Php $totalPrice"
                quantityTextView.text = "$totalQuantity"
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                totalQuantity = seekBar!!.progress
                totalPrice = (product.price)!! * totalQuantity
                priceTextView.text = "Php $totalPrice"
                quantityTextView.text = "$totalQuantity"

            }
        })
    }

    private fun initValues(product: Product) {
        Picasso.get().load(product.productImg).into(productImageView)
        producNameTextView.text = "${product.productName}"
        productWeightTextView.text = "1pc (${product.weight})"
        priceTextView.text = "Php ${product.price}"
        totalPrice = 1 * product.price!!
    }

    private fun init() {
        productImageView = findViewById(R.id.productImageView)
        producNameTextView = findViewById(R.id.productNameTextView)
        productWeightTextView = findViewById(R.id.productQuantityTextView)
        quantityTextView = findViewById(R.id.quantityNumber)
        quantitySeekBar = findViewById(R.id.quantitySeekBar)
        priceTextView = findViewById(R.id.productPriceTextView)
        button = findViewById(R.id.addToCartButton)
    }


}