package com.android.example.pickreci.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pickreci.ItemViews.ProductItem
import com.android.example.pickreci.Models.Product
import com.android.example.pickreci.Models.RecipeModel
import com.android.example.pickreci.Palengke.Palengke
import com.android.example.pickreci.Palengke.ProductDetailsActivity
import com.android.example.pickreci.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class ProductActivity : AppCompatActivity() {

    //Rv
    private lateinit var recyclerView: RecyclerView
    private var adapter = GroupAdapter<ViewHolder>()


    //Button
    private lateinit var button: Button

    companion object {
        const val TAG = "ProductActivityTag"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        init()
        initRecyclerView()
        fetchProducts()
        initButton()
    }

    private fun initButton() {
        button.setOnClickListener {
            val product = Product(
                productName = "create_mode"
            )
            val intent = Intent(applicationContext, AddProductActivity::class.java)
            intent.putExtra(TAG, product)
            startActivity(intent)
        }
    }

    private fun fetchProducts() {
        val ref = FirebaseDatabase.getInstance().getReference("/products")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                adapter.clear()
                snapshot.children.forEach { product ->
                    val productData = product.getValue(Product::class.java)!!
                    adapter.add(ProductItem(productData))
                }

                adapter.setOnItemClickListener { item, view ->
                    val productItem = item as ProductItem
                    val productClicked = productItem.product
                    Log.i(TAG, "${productClicked.productName} : ${productClicked.uid}")
                    showPopupMenu(view, productClicked)

                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun init() {
        button = findViewById(R.id.button6)
    }


    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.productsRvagain)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter


    }

    private fun showPopupMenu(view: View, product: Product) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.edit_delete, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.delete_menu -> {
                    val ref = FirebaseDatabase.getInstance().getReference("products/${product.uid}")
                    ref.removeValue()
                    Toast.makeText(this, "Recipe deleted", Toast.LENGTH_SHORT).show()
                }
                R.id.edit_menu -> {
                    val intent = Intent(applicationContext, AddProductActivity::class.java)
                    intent.putExtra(TAG, product)
                    startActivity(intent)

                }
            }
            true
        })
        popupMenu.show()
    }
}