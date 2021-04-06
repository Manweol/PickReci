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
import com.android.example.pickreci.SpecificDetailsActivity
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
        initListeners()



        return  v




    }

    private fun initListeners() {
        //adapter listener
        adapter.setOnItemClickListener { item, view ->
            val recipeItem = item as RecipeItem
            val recipeClicked = recipeItem.recipe
            val intent  = Intent(v.context, SpecificDetailsActivity::class.java )
            intent.putExtra(TAG, recipeClicked)
            startActivity(intent)
        }
    }

    private fun fetchRecipes() {
        val adobo = RecipeModel(
            uid = "1",
            title =  "Adobo",
            instructions = "The Moon is a barren, rocky world without air and water. It has dark lava plain on its surface. The Moon is filled wit craters. It has no light of its own. It gets its light from the Sun. The Moo keeps changing its shape as it moves round the Earth. It spins on its axis in 27.3 days stars were named after the Edwin Aldrin were the first ones to set their foot on the Moon on 21 July 1969 They reached the Moon in their space craft named Apollo II.",
            ingredients = "The sun is a huge ball of gases. It has a diameter of 1,392,000 km. It is so huge that it can hold millions of planets inside it. The Sun is mainly made up of hydrogen and helium gas. The surface of the Sun is known as the photosphere. The photosphere is surrounded by a thin layer of gas known as the chromospheres. Without the Sun, there would be no life on Earth. There would be no plants, no animals and no human beings. As, all the living things on Earth get their energy from the Sun for their survival.",
            imageURL = "https://panlasangpinoy.com/wp-content/uploads/2019/10/easy-chicken-adobo.jpg")
        adapter.add(RecipeItem(adobo))
        adapter.add(RecipeItem(adobo))
        adapter.add(RecipeItem(adobo))
        adapter.add(RecipeItem(adobo))
        adapter.add(RecipeItem(adobo))
        adapter.add(RecipeItem(adobo))
        adapter.add(RecipeItem(adobo))

    }

    private fun init() {
        recyclerView = v.findViewById(R.id.recyclerView_recipe)
        recyclerView.addItemDecoration(DividerItemDecoration(v.context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter



    }

}