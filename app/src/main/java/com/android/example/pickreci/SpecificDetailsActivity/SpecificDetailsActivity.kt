package com.android.example.pickreci.SpecificDetailsActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.*
import com.android.example.pickreci.Models.RecipeModel
import com.android.example.pickreci.R
import com.android.example.pickreci.Recipe.Recipe
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class SpecificDetailsActivity : AppCompatActivity() {
    private lateinit var instructionsBtn: Button
    private lateinit var ingredientsBtn: Button
    private lateinit var backBtn: ImageButton
    private lateinit var slider: ImageSlider
    private lateinit var listView: ListView
    private lateinit var titleTv: TextView
    var instructionsList: ArrayList<String> = ArrayList()
    var ingredientsList: ArrayList<String> = ArrayList()
    lateinit var instructionsAdapter: ArrayAdapter <String>
    lateinit var ingredientsAdapter: ArrayAdapter <String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specific_details)
        val recipe = intent.getParcelableExtra<RecipeModel>(Recipe.TAG)!!
        init()
        initBackBtnListener()
        initImageSlider(recipe)
        fetchInstructions(recipe)
        setRecipeTitle(recipe.title)

        //btn listeners
        instructionsBtn.setOnClickListener {
            fetchInstructions(recipe)
        }
        ingredientsBtn.setOnClickListener {
            fetchIngredients(recipe)
        }

    }

    private fun setRecipeTitle(title: String?) {
        titleTv.text=  title.toString()
    }

    private fun fetchInstructions(recipe: RecipeModel) {
        disableButton(instructionsBtn)
        enableButton(ingredientsBtn)
        instructionsList.clear()
        val ref = FirebaseDatabase.getInstance().getReference("instructions/${recipe.uid}")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { instruction ->
                    instructionsList.add(instruction.value.toString())
                    Log.i(TAG, "Key: ${instruction.key} || Value: ${instruction.value}")
                }
                listView.adapter = instructionsAdapter
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun enableButton(btn: Button) {
        btn.isEnabled = true
        btn.alpha = 1f

    }

    private fun disableButton(btn: Button) {
        btn.isEnabled = false
        btn.alpha = .5f
    }

    private fun fetchIngredients(recipe: RecipeModel) {
        disableButton(ingredientsBtn)
        enableButton(instructionsBtn)
        ingredientsList.clear()
        val ref = FirebaseDatabase.getInstance().getReference("ingredient/${recipe.uid}")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { ingredient ->
                    ingredientsList.add(ingredient.value.toString())
                    Log.i(TAG, "Key: ${ingredient.key} || Value: ${ingredient.value}")
                }
                listView.adapter = ingredientsAdapter


            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }



    private fun initBackBtnListener() {
        backBtn.setOnClickListener {
            finish()
        }

    }



    private fun init() {
        titleTv = findViewById(R.id.title_recipe)
        slider = findViewById(R.id.slider)
        backBtn = findViewById(R.id.imageButton)
        instructionsBtn= findViewById(R.id.button5_instructions)
        ingredientsBtn = findViewById(R.id.button6_ingredients)
        listView = findViewById(R.id.lv)
        ingredientsAdapter =  ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, ingredientsList)
        instructionsAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, instructionsList)

    }

    private fun initImageSlider(recipe: RecipeModel) {
        val remoteImages: ArrayList<SlideModel> = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("/slider-images/${recipe.uid}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { image ->
                    remoteImages.add(SlideModel(image.child("imageURL").value.toString(), "", ScaleTypes.CENTER_CROP))
                }
                slider.setImageList(remoteImages, ScaleTypes.CENTER_CROP)
                slider.stopSliding()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        slider.stopSliding()

    }

    companion object {
        const val TAG = "SpecificDetailsAcitivty"
    }
}