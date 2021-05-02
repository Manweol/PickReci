package com.android.example.pickreci.Market

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.GridView
import com.android.example.pickreci.MainAdapter
import com.android.example.pickreci.R

class ProductListActivity : AppCompatActivity() {

    var fruitNames = arrayOf("Apple", "Banana", "Mango","Mangosteen", "Orange","Persimmon","Pineapple","Strawberry","Watermelon")
    var fruitImages = intArrayOf(R.drawable.apple, R.drawable.banana, R.drawable.mango, R.drawable.mangosteen
            , R.drawable.orange, R.drawable.persimmon, R.drawable.pineapple, R.drawable.strawberry, R.drawable.watermelon)

    var poultryNames = arrayOf("Beef", "Chicken Drumstick", "Longganisa", "Pork Belly", "Pork Loin", "Pork Sinigang","Whole Chicken", "Chicken Wings")
    var poultryImages = intArrayOf(R.drawable.beef, R.drawable.drumstick, R.drawable.longganisa, R.drawable.porkbelly, R.drawable.porkloin,
            R.drawable.porksinigang, R.drawable.wholechicken, R.drawable.wings)

    var seafoodNames = arrayOf("Crab", "Hasa-Hasa", "Octopus", "Salmon", "Shrimp", "Squid", "Tahong", "Tilapia")
    var seafoodImages = intArrayOf(R.drawable.crab, R.drawable.hasahasa, R.drawable.octopus, R.drawable.salmon, R.drawable.shrimp,
            R.drawable.squid, R.drawable.tahong, R.drawable.tilapia)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        val category = intent.extras!!.getString("category")

        val gridView = findViewById<GridView>(R.id.gridView)

        if(category == "F R U I T S") {
            val mainAdapter = MainAdapter(this , fruitNames , fruitImages)
            gridView.adapter = mainAdapter
            gridView.onItemClickListener = AdapterView.OnItemClickListener { _ , _ , position , id ->
                val intent = Intent(this, ProductDetailsActivity::class.java)
                intent.putExtra("position", fruitNames[position])
                startActivity(intent)
            }
        }else if (category == "P O U L T R Y"){
            val mainAdapter = MainAdapter(this , poultryNames , poultryImages)
            gridView.adapter = mainAdapter
            gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, id ->
                val intent = Intent(this, ProductDetailsActivity::class.java)
                intent.putExtra("position", poultryNames[position])
                startActivity(intent)
            }
        }
        else if (category == "S E A F O O D"){
            val mainAdapter = MainAdapter(this , seafoodNames , seafoodImages)
            gridView.adapter = mainAdapter
            gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, id ->
                val intent = Intent(this, ProductDetailsActivity::class.java)
                intent.putExtra("position", seafoodNames[position])
                startActivity(intent)
            }
        }

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