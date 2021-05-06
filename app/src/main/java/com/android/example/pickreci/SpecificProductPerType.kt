package com.android.example.pickreci

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pickreci.ItemViews.ProductItem
import com.android.example.pickreci.ItemViews.TypeItem
import com.android.example.pickreci.Models.ProductModel
import com.android.example.pickreci.Palengke.Palengke
import com.android.example.pickreci.Palengke.ProductDetailsActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class SpecificProductPerType : AppCompatActivity() {

        lateinit var recyclerView: RecyclerView
        lateinit var toolBar: Toolbar
        val adapter = GroupAdapter<ViewHolder>()
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.specific_product_per_type)
            val type = intent.getStringExtra(TypeItem.TAG)!!
            init()
            fetchProduct(type)
            setToolBarTitle(type)
            setToolBarListener()

        }

        private fun setToolBarListener() {
            toolBar.setNavigationOnClickListener {
                finish()
            }
        }

        private fun setToolBarTitle(type: String) {
            toolBar.title = type.toUpperCase()
        }

        private fun fetchProduct( type: String) {
            val ref = FirebaseDatabase.getInstance().getReference("products")
            ref.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { products ->
                        val productData = products.getValue(ProductModel::class.java)!!
                        if (productData.type == type) {
                            adapter.add(ProductItem(productData))
                        }
                    }

                    adapter.setOnItemClickListener { item, view ->
                        val productItem = item as ProductItem
                        val intent = Intent(this@SpecificProductPerType, ProductDetailsActivity::class.java)
                        intent.putExtra(Palengke.TAG, productItem.productModel )
                        startActivity(intent)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        private fun init() {
            recyclerView = findViewById(R.id.rvagain)
            recyclerView.adapter =  adapter
            toolBar = findViewById(R.id.toolbar2)

        }
        companion object {
            const val TAG = "SpecificProductPerTypeActivity"
        }
    }
