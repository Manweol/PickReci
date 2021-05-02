package com.android.example.pickreci.Palengke

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.example.pickreci.Models.CartProduct
import com.android.example.pickreci.Models.Product
import com.android.example.pickreci.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : AppCompatActivity() {


    private lateinit var productImageView: ImageView
    private lateinit var producNameTextView: TextView
    private lateinit var productWeightTextView: TextView
    private lateinit var quantityTextView: TextView
    private lateinit var quantitySeekBar: SeekBar
    private lateinit var priceTextView: TextView
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        val product = intent.getParcelableExtra<Product>(Palengke.TAG)!!
        init()
        initValues(product)
        initSeekBar()
        initAddToCartButton(product)


    }

    private fun initAddToCartButton(product: Product) {
        button.setOnClickListener {
            if (quantityTextView.text != "0") {
                //Add the product to cart
                addToCart(product)
            } else {
                Toast.makeText(this, "Quantity must not be 0", Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun addToCart(product: Product) {
//        var cartProduct = CartProduct(uid = null, productUid =  product.uid, quantity = quantityTextView.text.toString().toInt() )
    }

    private fun initSeekBar() {
        quantitySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                quantityTextView.text = "$progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                quantityTextView.text = "${seekBar!!.progress}"
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                quantityTextView.text = "${seekBar!!.progress}"

            }
        })
    }

    private fun initValues(product: Product) {
        Picasso.get().load(product.productImg).into(productImageView)
        producNameTextView.text = "${product.productName}"
        productWeightTextView.text = "1pc (${product.weight})"
        priceTextView.text = "Php ${product.price}"
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