package com.android.example.pickreci

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.android.example.pickreci.Models.RecipeModel
import com.android.example.pickreci.Recipe.Recipe
import com.squareup.picasso.Picasso

class SpecificDetailsActivity : AppCompatActivity() {
    private lateinit var image: ImageView
    private lateinit var instructions: TextView
    private lateinit var ingredients: TextView
    private lateinit var title: TextView
    private lateinit var backBtn: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specific_details)
        val recipe = intent.getParcelableExtra<RecipeModel>(Recipe.TAG)!!
        init()
        initListeners()
        changeValues(recipe)



    }

    private fun initListeners() {
        backBtn.setOnClickListener {
            finish()
        }
    }

    private fun changeValues(recipe: RecipeModel) {

        Picasso.get().load(recipe.imageURL).into(image)
        ingredients.text = recipe.ingredients
        instructions.text = recipe.instructions
        title.text = recipe.title
    }

    private fun init() {
        instructions = findViewById(R.id.instructionsTextView)
        ingredients = findViewById(R.id.ingredientsTextView)
        image = findViewById(R.id.imageView_specific)
        title = findViewById(R.id.recipeTitle_specific)
        backBtn = findViewById(R.id.imageButton)

    }
}