package com.android.example.pickreci.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pickreci.ItemViews.RecipeItem
import com.android.example.pickreci.Models.RecipeModel
import com.android.example.pickreci.R
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class RecipeActivity : AppCompatActivity() {
    private lateinit var rv: RecyclerView
    private lateinit var addBtn: Button
    var adapter = GroupAdapter<ViewHolder>()
    companion object {
        const val TAG = "AdminActivityTag"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        init()
        initAddBtn()
        fetchRecipes()
    }

    private fun initAddBtn() {
        addBtn.setOnClickListener {
            val model = RecipeModel()
            val intent = Intent(applicationContext, AddRecipeActivity::class.java)
            intent.putExtra(TAG, model)
            startActivity(intent)

        }
    }

    private fun fetchRecipes() {

        val ref = FirebaseDatabase.getInstance().getReference("recipe")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                adapter.clear()
                snapshot.children.forEach { recipe ->
                    val recipeData = recipe.getValue(RecipeModel::class.java)!!
                    adapter.add(RecipeItem(recipeData))
                    //adapter listener
                    adapter.setOnItemClickListener { item, view ->
                        val recipeItem = item as RecipeItem
                        val recipeClicked = recipeItem.recipe

                        showPopupMenu(view, recipeClicked )
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun showPopupMenu(view: View, recipeClicked: RecipeModel) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.edit_delete, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.delete_menu -> {
                    val recipeRef =
                        FirebaseDatabase.getInstance().getReference("recipe/${recipeClicked.uid}")
                    val insRef = FirebaseDatabase.getInstance()
                        .getReference("instructions/${recipeClicked.uid}")
                    val ingRef = FirebaseDatabase.getInstance()
                        .getReference("ingredient/${recipeClicked.uid}")
                    val sliderRef = FirebaseDatabase.getInstance()
                        .getReference("slider-images/${recipeClicked.uid}")
                    recipeRef.removeValue()
                    insRef.removeValue()
                    ingRef.removeValue()
                    sliderRef.removeValue()
                    Toast.makeText(this, "Recipe deleted", Toast.LENGTH_SHORT).show()
                }
                R.id.edit_menu -> {
                    val intent = Intent(this@RecipeActivity, AddRecipeActivity::class.java)
                    intent.putExtra(TAG, recipeClicked)
                    startActivity(intent)

                }
            }
            true
        })
        popupMenu.show()
    }


    private fun init() {
        rv = findViewById(R.id.rv_admin)
        rv.adapter = adapter
        addBtn = findViewById(R.id.btn_admin)
    }
}