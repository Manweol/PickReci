package com.android.example.pickreci.Market

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.example.pickreci.Market.CartActivity
import com.android.example.pickreci.R

var addedToCart = arrayListOf<String>()

class ProductDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        val position = intent.extras!!.getString("position")
        val productImage = findViewById<ImageView>(R.id.productImageView)
        val productName = findViewById<TextView>(R.id.productNameTextView)
        val productQuantity = findViewById<TextView>(R.id.productQuantityTextView)
        findViewById<SeekBar>(R.id.quantitySeekBar).setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val quantity = findViewById<TextView>(R.id.quantityNumber) as TextView
                quantity.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        val productPrice = findViewById<TextView>(R.id.productPriceTextView)

//FRUITS SECTION
        when {
            position.equals("Apple") -> {
                productImage.setImageResource(R.drawable.apple2)
                productName.text = position
                productQuantity.text = "1 pc (100- 120 grams)"
                productPrice.text = "Php 25.00"
                findViewById<Button>(R.id.addToCartButton).setOnClickListener{
                    Toast.makeText(this, "Added To Cart", Toast.LENGTH_SHORT).show()
                    addedToCart.add(position.toString())
                }

            }
            position.equals("Banana") -> {
                productImage.setImageResource(R.drawable.banana2)
                productName.text = position
                productQuantity.text = "1 branch (800 grams - 1 kg)"
                productPrice.text = "Php 80.00"
                findViewById<Button>(R.id.addToCartButton).setOnClickListener{
                    Toast.makeText(this, "Added To Cart", Toast.LENGTH_SHORT).show()
                    addedToCart.add(position.toString())
                }
            }
            position.equals("Mango") -> {
                productImage.setImageResource(R.drawable.mango2)
                productName.text = position
                productQuantity.text = "1 pc (100 - 200 grams)"
                productPrice.text = "Php 30.00"
                findViewById<Button>(R.id.addToCartButton).setOnClickListener{
                    Toast.makeText(this, "Added To Cart", Toast.LENGTH_SHORT).show()
                    addedToCart.add(position.toString())
                }
            }
            position.equals("Mangosteen") -> {
                productImage.setImageResource(R.drawable.mangosteen2)
                productName.text = position
                productQuantity.text = "3 pcs (300 - 450 grams)"
                productPrice.text = "Php 50.00"
                findViewById<Button>(R.id.addToCartButton).setOnClickListener{
                    Toast.makeText(this, "Added To Cart", Toast.LENGTH_SHORT).show()
                    addedToCart.add(position.toString())
                }
            }
            position.equals("Orange") -> {
                productImage.setImageResource(R.drawable.orange2)
                productName.text = position
                productQuantity.text = "1 pc (100 - 200 grams)"
                productPrice.text = "Php 15.00"
                findViewById<Button>(R.id.addToCartButton).setOnClickListener{
                    Toast.makeText(this, "Added To Cart", Toast.LENGTH_SHORT).show()
                    addedToCart.add(position.toString())
                }
            }
            position.equals("Persimmon") -> {
                productImage.setImageResource(R.drawable.persimmon2)
                productName.text = position
                productQuantity.text = "1 pc (100 - 200 grams)"
                productPrice.text = "Php 20.00"
                findViewById<Button>(R.id.addToCartButton).setOnClickListener{
                    Toast.makeText(this, "Added To Cart", Toast.LENGTH_SHORT).show()
                    addedToCart.add(position.toString())
                }
            }
            position.equals("Pineapple") -> {
                productImage.setImageResource(R.drawable.pineapple2)
                productName.text = position
                productQuantity.text = "1 pc (300 - 600 grams)"
                productPrice.text = "Php 70.00"
                findViewById<Button>(R.id.addToCartButton).setOnClickListener{
                    Toast.makeText(this, "Added To Cart", Toast.LENGTH_SHORT).show()
                    addedToCart.add(position.toString())
                }
            }
            position.equals("Strawberry") -> {
                productImage.setImageResource(R.drawable.strawberry2)
                productName.text = position
                productQuantity.text = "10 pcs (100 - 200 grams)"
                productPrice.text = "Php 80.00"
                findViewById<Button>(R.id.addToCartButton).setOnClickListener{
                    Toast.makeText(this, "Added To Cart", Toast.LENGTH_SHORT).show()
                    addedToCart.add(position.toString())
                }
            }
            position.equals("Watermelon") -> {
                productImage.setImageResource(R.drawable.watermelon2)
                productName.text = position
                productQuantity.text = "1 pc (500 - 800 grams)"
                productPrice.text = "Php 90.00"
                findViewById<Button>(R.id.addToCartButton).setOnClickListener{
                    Toast.makeText(this, "Added To Cart", Toast.LENGTH_SHORT).show()
                    addedToCart.add(position.toString())
                }
            }
        }

        Log.i("puta", "$addedToCart")

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.nav_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_cart -> {
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_recipe -> {
                true
            }
            R.id.nav_palengke -> {
                val intent = Intent(this , ProductsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.go_to_bills -> {
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

}