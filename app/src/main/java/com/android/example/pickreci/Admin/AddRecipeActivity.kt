package com.android.example.pickreci.Admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.example.pickreci.Constants
import com.android.example.pickreci.Models.RecipeModel
import com.android.example.pickreci.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList


class AddRecipeActivity : AppCompatActivity() {
    private lateinit var image: ImageView
    private lateinit var imageBtn: ImageButton
    private lateinit var title: EditText
    private lateinit var spinner: Spinner
    private lateinit var instructionsET: EditText
    private lateinit var instructionsBtn: Button
    private lateinit var instructionsListView: ListView
    private lateinit var ingredientET: EditText
    private lateinit var ingredientBtn: Button
    private lateinit var ingredientListView: ListView
    private lateinit var save: Button

    //Array Lists
    private var ingredientsArrayList: ArrayList<String> = ArrayList()
    private var instructionsArrayList: ArrayList<String> = ArrayList()
    private var imagesUriArrayList: ArrayList<Uri> = ArrayList()
    private var typesArrayList: ArrayList<String> = ArrayList()

    //Array Adapters
    private lateinit var ingredientsAdapter: ArrayAdapter<String>
    private lateinit var instructionsAdapter: ArrayAdapter<String>
    //constants

    companion object {
        const val IMAGE_PICK_CODE = 1001
        const val MULTIPLE_PICK_CODE = 1002
        const val TAG = "AddRecipeTag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)
        val recipe = intent.getParcelableExtra<RecipeModel>(RecipeActivity.TAG)!!

        //initilize everything here
        init()
        initSpinner()
        initInstructionsPart()
        initIngredientsPart()
        initImagePicker()
        initContextMenu()

        if (!recipe.uid.isNullOrEmpty()) {
            initValues(recipe)
        }
        initSave(recipe)
    }

    private fun initContextMenu() {
        registerForContextMenu(ingredientListView)
        registerForContextMenu(instructionsListView)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        if (v!!.id == R.id.instructionsListView_add) {
            inflater.inflate(R.menu.delete_ins, menu)
        }
        if (v.id == R.id.ingredients_listView_add) {
            inflater.inflate(R.menu.delete_ing, menu)
        }

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo

        return when (item.itemId) {
            R.id.delete_menu_ins -> {
                instructionsArrayList.removeAt(info.position)
                instructionsAdapter.notifyDataSetChanged()
                true
            }
            R.id.delete_menu_ing -> {
                ingredientsArrayList.removeAt(info.position)
                ingredientsAdapter.notifyDataSetChanged()
                true
            }
            else -> super.onContextItemSelected(item)
        }

    }

    private fun initValues(recipe: RecipeModel) {

        title.setText(recipe.title)

        val insRef = FirebaseDatabase.getInstance().getReference("instructions/${recipe.uid}")
        insRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { instruction ->
                    instructionsArrayList.add(instruction.value.toString())
                    instructionsAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        val ingRef = FirebaseDatabase.getInstance().getReference("ingredient/${recipe.uid}")
        ingRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { ingredient ->
                    ingredientsArrayList.add(ingredient.value.toString())
                    ingredientsAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun initSave(recipe: RecipeModel) {
        save.setOnClickListener {
            if (selectedPhotoUri == null) {
                Toast.makeText(
                    applicationContext,
                    "Please select the main image.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (title.text.toString().isNullOrEmpty()) {
                title.error = "Add title"
                title.requestFocus()
                return@setOnClickListener
            }
            if (imagesUriArrayList.isEmpty()) {
                Toast.makeText(
                    this,
                    "Choose at least 1 image to be displayed in the slider.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (instructionsArrayList.isEmpty()) {
                Toast.makeText(this, "Add at least 1 instruction", Toast.LENGTH_SHORT).show()
                instructionsET.requestFocus()
                return@setOnClickListener
            }
            if (ingredientsArrayList.isEmpty()) {
                Toast.makeText(this, "Add at least 1 ingredient", Toast.LENGTH_SHORT).show()
                ingredientET.requestFocus()
                return@setOnClickListener
            }

            //if everything's okay, upload to the database
            if (recipe.uid == null || recipe.uid == "") {
                val ref = FirebaseDatabase.getInstance().getReference("recipe")
                val recipeUid = ref.push().key
                Log.i(TAG, recipeUid!!)
                uploadToFirebase(recipeUid!!)
            } else {
                uploadToFirebase(recipe.uid!!)
            }
        }
    }


    private fun uploadToFirebase(recipeUid: String) {

        //upload the singe image first
        val fileName = UUID.randomUUID().toString()
        val storageRef = FirebaseStorage.getInstance().getReference("images/${fileName}")
        storageRef.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    uploadToFirebaseDatabase(uri.toString(), recipeUid)
                }
            }

    }

    private fun uploadToFirebaseDatabase(imageUrl: String, recipeUid: String) {
        val ref = FirebaseDatabase.getInstance().getReference("recipe")

        val recipe = RecipeModel(
            uid = recipeUid,
            title = title.text.toString(),
            type = spinner.selectedItem.toString(),
            imageURL = imageUrl
        )
        ref.child(recipeUid).setValue(recipe)


        //upload multiple images
        val databaseRef =
            FirebaseDatabase.getInstance().getReference("/slider-images/${recipe.uid}")
        databaseRef.removeValue()
        imagesUriArrayList.forEach { uri ->
            val fileName = UUID.randomUUID().toString()
            val storageRef = FirebaseStorage.getInstance().getReference("/slider-images/$fileName")
            storageRef.putFile(uri)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { uriNeeded ->
                        val key = databaseRef.push().key
                        databaseRef.child(key!!).child("imageURL").setValue(uriNeeded.toString())
                    }
                }
        }
        //upload the ingredients
        val ingredientsRef = FirebaseDatabase.getInstance().getReference("ingredient/${recipeUid}")
        ingredientsRef.removeValue()
        ingredientsArrayList.forEachIndexed { index, ingredient ->
            ingredientsRef.child("$index").setValue(ingredient)
            Log.i(TAG, index.toString())
        }
        //upload instructions
        val instructionsRef =
            FirebaseDatabase.getInstance().getReference("instructions/${recipeUid}")
        instructionsRef.removeValue()
        instructionsArrayList.forEachIndexed { index, instruction ->
            instructionsRef.child("${index}").setValue(instruction)
            Log.i(TAG, index.toString())
        }

        finish()
        Toast.makeText(applicationContext, "Recipe created.", Toast.LENGTH_SHORT).show()

    }

    private fun initImagePicker() {
        image.setOnClickListener {
            pickImageFromGallery(IMAGE_PICK_CODE)
        }
        imageBtn.setOnClickListener {
            pickImageFromGallery(MULTIPLE_PICK_CODE)
        }

    }

    private fun pickImageFromGallery(imagePickCode: Int) {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, imagePickCode)
    }

    private var selectedPhotoUri: Uri? = null
    private var multiplePhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            selectedPhotoUri = data!!.data
            image.setImageURI(selectedPhotoUri)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == MULTIPLE_PICK_CODE) {
            multiplePhotoUri = data!!.data
            imagesUriArrayList.add(multiplePhotoUri!!)
            Toast.makeText(this, "There are ${imagesUriArrayList.size} added.", Toast.LENGTH_SHORT)
                .show()
        }

    }

    private fun initIngredientsPart() {
        ingredientListView.adapter = ingredientsAdapter

        ingredientBtn.setOnClickListener {
            //check value
            if (ingredientET.text.toString().isNullOrEmpty()) {
                ingredientET.error = "Add ingredient"
                ingredientET.requestFocus()
                return@setOnClickListener
            }
            //add
            ingredientsArrayList.add(ingredientET.text.toString())
            ingredientsAdapter.notifyDataSetChanged()
            //delete text
            ingredientET.text.clear()
            Toast.makeText(this, "Ingredient added", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initInstructionsPart() {
        //
        instructionsListView.adapter = instructionsAdapter

        instructionsBtn.setOnClickListener {
            //check if there is a value
            if (instructionsET.text.toString().isNullOrEmpty()) {
                instructionsET.error = "Add instruction"
                instructionsET.requestFocus()
                return@setOnClickListener
            }
            //add the text to the Array List
            instructionsArrayList.add(instructionsET.text.toString())
            instructionsAdapter.notifyDataSetChanged()
            //delete text
            instructionsET.text.clear()
            Toast.makeText(this, "Instruction added", Toast.LENGTH_SHORT).show()
        }


    }

    private fun initSpinner() {
        typesArrayList.add(Constants.BREAKFAST)
        typesArrayList.add(Constants.MERYENDA)
<<<<<<< HEAD
        typesArrayList.add(Constants.LUNCHANDDINNER)
        typesArrayList.add(Constants.SEAFOOD)
        typesArrayList.add(Constants.SEASONING)
        typesArrayList.add(Constants.POULTRY)
=======
        typesArrayList.add(Constants.LINNER)
        typesArrayList.add(Constants.POULTRY)
        typesArrayList.add(Constants.SEAFOOD)
        typesArrayList.add(Constants.SEASONING)
>>>>>>> e385fa669314b2cf4a10f5a156b7394185ab24af
        typesArrayList.add(Constants.VEGETABLES)

        //Add types to spinner
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typesArrayList)
        spinner.adapter = arrayAdapter


    }

    private fun init() {
        image = findViewById(R.id.image_add)
        imageBtn = findViewById(R.id.multipleImage_add)
        title = findViewById(R.id.title_add)
        spinner = findViewById(R.id.type_add)
        instructionsET = findViewById(R.id.insEditText_add)
        instructionsBtn = findViewById(R.id.instructionsBtn_add)
        instructionsListView = findViewById(R.id.instructionsListView_add)
        ingredientET = findViewById(R.id.ingredientsET_add)
        ingredientBtn = findViewById(R.id.ingredientsBtn_add)
        ingredientListView = findViewById(R.id.ingredients_listView_add)
        save = findViewById(R.id.save_add)

        instructionsAdapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_list_item_1,
            instructionsArrayList
        )
        ingredientsAdapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_list_item_1,
            ingredientsArrayList
        )

    }
}