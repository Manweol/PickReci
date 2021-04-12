package com.android.example.pickreci

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pickreci.ItemViews.RecipeItem
import com.android.example.pickreci.ItemViews.TypeItem
import com.android.example.pickreci.Models.RecipeModel
import com.android.example.pickreci.Recipe.Recipe
import com.android.example.pickreci.SpecificDetailsActivity.SpecificDetailsActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_specific_recipe_per_type.*

class SpecificRecipePerType : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var toolBar: Toolbar
    val adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specific_recipe_per_type)
        val type = intent.getStringExtra(TypeItem.TAG)!!
        init()
        fetchRecipe(type)
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

    private fun fetchRecipe( type: String) {
        val ref = FirebaseDatabase.getInstance().getReference("recipe")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { recipe ->
                    val recipeData = recipe.getValue(RecipeModel::class.java)!!
                    if (recipeData.type == type) {
                        adapter.add(RecipeItem(recipeData))
                    }
                }

                adapter.setOnItemClickListener { item, view ->
                    val recipeItem = item as RecipeItem
                    val intent = Intent(this@SpecificRecipePerType, SpecificDetailsActivity::class.java)
                    intent.putExtra(Recipe.TAG, recipeItem.recipe )
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
        const val TAG = "SpecificRecipePerTypeActivity"
    }
}