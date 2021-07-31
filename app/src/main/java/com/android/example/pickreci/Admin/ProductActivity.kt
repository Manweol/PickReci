
package com.android.example.pickreci.Admin

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pickreci.InitialScreen.InitialScreen
import com.android.example.pickreci.ItemViews.ProductItem
import com.android.example.pickreci.LoginScreen.LoginScreenActivity
import com.android.example.pickreci.Models.ProductModel
import com.android.example.pickreci.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import javax.security.auth.login.LoginException

class ProductActivity : AppCompatActivity() {

    //Rv
    private lateinit var recyclerView: RecyclerView
    private var adapter = GroupAdapter<ViewHolder>()


    //Button
    private lateinit var button: Button

    companion object {
        const val TAG = "AdminActivityTag"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        init()
        initRecyclerView()
        fetchProducts()
        initButton()
        checkIfLoggedIn()

        var signout = findViewById<Button>(R.id.signout)

        signout.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage("Do you want to sign out now?")
                .setCancelable(true)
                .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, LoginScreenActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    Toast.makeText(
                        baseContext, "Signed out",
                        Toast.LENGTH_LONG
                    ).show()


                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, _ ->
                    dialog.cancel()
                })
            val alert = dialogBuilder.create()
            alert.setTitle("Sign out")
            alert.show()
        }

    }


    private fun checkIfLoggedIn() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            val intent = Intent(this, LoginScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun initButton() {
        button.setOnClickListener {
            val product = ProductModel(
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
                    val productData = product.getValue(ProductModel::class.java)!!
                    adapter.add(ProductItem(productData))
                }

                adapter.setOnItemClickListener { item, view ->
                    val productItem = item as ProductItem
                    val productClicked = productItem.productModel
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

    private fun showPopupMenu(view: View, productModel: ProductModel) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.edit_delete, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.delete_menu -> {
                    val ref = FirebaseDatabase.getInstance().getReference("products/${productModel.uid}")
                    ref.removeValue()
                    Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show()
                }
                R.id.edit_menu -> {
                    val intent = Intent(applicationContext, AddProductActivity::class.java)
                    intent.putExtra(TAG, productModel)
                    startActivity(intent)

                }
            }
            true
        })
        popupMenu.show()
    }

}