package com.android.example.pickreci.Palengke

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pickreci.DeleteLater
import com.android.example.pickreci.ItemViews.ProductItem
import com.android.example.pickreci.Models.Product
import com.android.example.pickreci.R
import com.android.example.pickreci.Recipe.Recipe
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class Palengke : Fragment() {


    private lateinit var recyclerView: RecyclerView
    private var adapter = GroupAdapter<ViewHolder>()
    private lateinit var v: View

    //searchView
    private lateinit var searchView: SearchView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_palengke, container, false)
        init()
        initRecyclerView()
        fetchProducts()
        initSearchView()


        return v
    }

    private fun initSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                fetchQuery(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == null || newText == "") {
                    fetchProducts()
                } else {
                    fetchQuery(newText)
                }
                return false
            }
        })
    }

    private fun fetchQuery(query: String) {
        val ref = FirebaseDatabase.getInstance().getReference("/products")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                adapter.clear()
                snapshot.children.forEach { product ->
                    val productData = product.getValue(Product::class.java)!!
                    if (productData.productName!!.contains(query, ignoreCase = true)) {
                        adapter.add(ProductItem(productData))
                    }
                    if (adapter.getItemCount() == 0) {
                        Toast.makeText(v.context, "No ${query} found.", Toast.LENGTH_SHORT).show()
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun init() {
        searchView = v.findViewById(R.id.searchView)
    }

    private fun fetchProducts() {
        val ref = FirebaseDatabase.getInstance().getReference("/products")
        ref.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    adapter.clear()
                    snapshot.children.forEach { product ->
                        val productData = product.getValue(Product::class.java)!!
                        adapter.add(ProductItem(productData))
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                }
            }
        )

    }

    private fun initRecyclerView() {
        recyclerView = v.findViewById(R.id.recyclerView_product)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                v.context,
                DividerItemDecoration.VERTICAL
            )
        )
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { item, view ->
            val productItem = item as ProductItem
            val productClicked = productItem.product
            val intent = Intent(v.context, ProductDetailsActivity::class.java)
            intent.putExtra(TAG, productClicked)
            startActivity(intent)
        }


    }

    companion object {
        const val TAG = "PalengkeTag"
    }
}