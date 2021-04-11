package com.android.example.pickreci.Recipe

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pickreci.ItemViews.RecipeItem
import com.android.example.pickreci.Models.RecipeModel
import com.android.example.pickreci.R
import com.android.example.pickreci.SpecificDetailsActivity.SpecificDetailsActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder


class Recipe : Fragment() {
    private lateinit var v: View
    private lateinit var recyclerView : RecyclerView
    val adapter = GroupAdapter<ViewHolder>()


    companion object {
        const val TAG = "RecipeTag"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_recipe, container, false)
        init()
        fetchRecipes()

        return  v


    }

    private fun fetchRecipes() {
        val ref = FirebaseDatabase.getInstance().getReference("recipe")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                adapter.clear()
                snapshot.children.forEach { recipe ->
                    val recipeData = recipe.getValue(RecipeModel::class.java)!!
                    adapter.add(RecipeItem(recipeData))
                    //adapter listener
                    adapter.setOnItemClickListener { item, view ->
                        val recipeItem = item as RecipeItem
                        val recipeClicked = recipeItem.recipe
                        val intent  = Intent(v.context, SpecificDetailsActivity::class.java )
                        intent.putExtra(TAG, recipeClicked)
                        startActivity(intent)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })





    }

    private fun init() {
        recyclerView = v.findViewById(R.id.recyclerView_recipe)
        recyclerView.addItemDecoration(DividerItemDecoration(v.context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter



    }

}