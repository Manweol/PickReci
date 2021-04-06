package com.android.example.pickreci.ItemViews

import android.widget.ImageView
import android.widget.TextView
import com.android.example.pickreci.Models.RecipeModel
import com.android.example.pickreci.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class RecipeItem ( val recipe: RecipeModel): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        //title
        viewHolder.itemView.findViewById<TextView>(R.id.recipeTextView).text = recipe.title
        //image
        val recipeImage = viewHolder.itemView.findViewById<ImageView>(R.id.recipeImageView)
        //load image url to the image view
        if (!recipe.imageURL.isNullOrEmpty()){
            Picasso.get().load(recipe.imageURL).into(recipeImage)
        }

    }

    override fun getLayout(): Int {
        return R.layout.row_recipe
    }
}