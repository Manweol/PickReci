package com.android.example.pickreci.Recipe

import android.content.Intent
import android.os.Bundle
import android.renderscript.Sampler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pickreci.ItemViews.RecipeItem
import com.android.example.pickreci.Models.Ingredient
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
    private lateinit var searchView: SearchView
    val adapter = GroupAdapter<ViewHolder>()
    val adapter2 = GroupAdapter<ViewHolder>()
    //sampol lang
    var arrayList: ArrayList<String> = ArrayList()


    companion object {
        const val TAG = "RecipeTag"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_recipe, container, false)
        init()
        fetchRecipes()
        setTitle()
        initSearchView()

        return  v


    }


    private fun initSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                //fetch ingredients based from query
                fetchQuery(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    fetchRecipes()
                }
                return false
            }

        })
    }

    private fun fetchQuery(query: String) {
        val ref = FirebaseDatabase.getInstance().getReference("ingredient")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                snapshot.children.forEach { recipe ->
                    recipe.children.forEach { ingredient ->
                        if (ingredient.value.toString().equals(query!!, ignoreCase = true)) {
                            arrayList.add(recipe.key.toString())
                        }
                    }

                }
                //fetch recipe with that ingredients
                if (arrayList.isNotEmpty()) {
                    adapter.clear()
                    arrayList.forEach { recipeUid ->
                        val ref2 = FirebaseDatabase.getInstance().getReference("recipe/$recipeUid")
                        ref2.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val recipeData = snapshot.getValue(RecipeModel::class.java)
                                adapter.add(RecipeItem(recipeData!!))
                                adapter.setOnItemClickListener { item, view ->
                                    val recipeItem = item as RecipeItem
                                    val recipeClicked = recipeItem.recipe
                                    val intent = Intent(v.context, SpecificDetailsActivity::class.java)
                                    intent.putExtra(TAG, recipeClicked)
                                    startActivity(intent)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }
                        })
                    }
                } else {
                    val ref3= FirebaseDatabase.getInstance().getReference("recipe")
                    ref3.addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            snapshot.children.forEach { recipe ->
                                val recipeData = recipe.getValue(RecipeModel::class.java)
                                if (recipeData!!.title!!.contains(query, ignoreCase = true)){
                                    arrayList.add(recipeData.uid.toString())
                                }
                            }
                            if (arrayList.isNotEmpty()){
                                adapter.clear()
                                arrayList.forEach { recipeUid ->
                                    val ref4 = FirebaseDatabase.getInstance().getReference("recipe/${recipeUid}")
                                    ref4.addListenerForSingleValueEvent(object: ValueEventListener{
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            val recipeDataAgain = snapshot.getValue(RecipeModel::class.java)
                                            adapter.add(RecipeItem(recipeDataAgain!!))
                                            adapter.setOnItemClickListener { item, view ->
                                                val recipeItem = item as RecipeItem
                                                val recipeClicked = recipeItem.recipe
                                                val intent = Intent(v.context, SpecificDetailsActivity::class.java)
                                                intent.putExtra(TAG, recipeClicked)
                                                startActivity(intent)
                                            }
                                        }
                                        override fun onCancelled(error: DatabaseError) {
                                        }
                                    })
                                }
                            } else {
                                Toast.makeText(v.context, "No ${query} found.", Toast.LENGTH_SHORT).show()
                                fetchRecipes()
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun setTitle() {
        (activity as AppCompatActivity)!!.supportActionBar!!.title = "RECIPE"
    }

    private fun fetchRecipes() {

        val ref = FirebaseDatabase.getInstance().getReference("recipe")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                adapter.clear()
                snapshot.children.forEach { recipe ->
                    val recipeData = recipe.getValue(RecipeModel::class.java)!!
                    adapter.add(RecipeItem(recipeData))
                    recyclerView.adapter = adapter
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
        searchView = v.findViewById(R.id.searchView)
        recyclerView = v.findViewById(R.id.recyclerView_recipe)
        recyclerView.addItemDecoration(DividerItemDecoration(v.context, DividerItemDecoration.VERTICAL))




    }



}