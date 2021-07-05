package com.android.example.pickreci.Admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.android.example.pickreci.Constants
import com.android.example.pickreci.Models.ProductModel
import com.android.example.pickreci.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.row_product.*

import java.util.*

class AddProductActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var imageButton: ImageButton
    private lateinit var spinner: Spinner
    private lateinit var name: EditText
    private lateinit var price: EditText
    private lateinit var weight: EditText
    private lateinit var vendor: EditText
    private lateinit var button: Button
    private lateinit var progressBar: ProgressBar
    var isCreateMode: Boolean = true;
    var imageUri: Uri? = null

    private var typesArrayList: ArrayList<String> = ArrayList()

    companion object {
        const val IMAGE_PICK_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        val product = intent.getParcelableExtra<ProductModel>(ProductActivity.TAG)!!
        init()
        if (product.productName != "create_mode") {
            initValues(product)
            isCreateMode = false;
        } else {
            isCreateMode = true;
        }
        initButton(product)
        initImageButton()
        //initSpinner()

    }

    private fun initImageButton() {
        imageButton.setOnClickListener {
            pickImageFromGallery(IMAGE_PICK_CODE)
        }
    }


    private fun pickImageFromGallery(imagePickCode: Int) {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, imagePickCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == AddRecipeActivity.IMAGE_PICK_CODE) {
            imageUri = data!!.data
            imageView.setImageURI(imageUri)
        }
    }


    private fun initButton(productModel: ProductModel) {
        button.setOnClickListener {
            progressBar.isVisible = true
            //check everything first
            if (vendor.text.toString().isNullOrEmpty()) {
                vendor.error = "Add Vendor"
                vendor.requestFocus()
                return@setOnClickListener
            }
            if (name.text.toString().isNullOrEmpty()) {
                name.error = "Add name"
                name.requestFocus()
                return@setOnClickListener
            }
            if (price.text.toString().isNullOrEmpty()) {
                price.error = "Invalid process"
                price.requestFocus()
                return@setOnClickListener
            }
            if (weight.text.toString().isNullOrEmpty()) {
                weight.error = "Add weight"
                weight.requestFocus()
                return@setOnClickListener
            }
            if (isCreateMode) {
                if (imageUri == null) {
                    Toast.makeText(this, "Please pick an image.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else {
                    uploadImage(imageUri!!)
                }
            } else {
                if (imageUri == null) {
                    updateProduct(productModel.productImg.toString(), productModel.uid.toString())
                } else {
                    uploadImageWithExistingUid(productModel.uid.toString(), imageUri!!)
                }
            }
        }
        progressBar.isVisible = false

    }

    private fun uploadImageWithExistingUid(uid: String, imageUri: Uri) {
        val ref = FirebaseStorage.getInstance().getReference("product-images/$uid")
        ref.putFile(imageUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { link ->
                    updateProduct(link.toString(), uid)
                }
            }

    }

    private fun uploadImage(imageUri: Uri) {
        val fileName: String = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("product-images/${fileName}")
        ref.putFile(imageUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { link ->
                    updateProduct(link.toString(), fileName)
                }
            }
    }

    private fun updateProduct(imageUrl: String, uid: String) {
        val ref = FirebaseDatabase.getInstance().getReference("/products/${uid}")
        val product = ProductModel(
            uid = uid,
            productImg = imageUrl,
            productName = name.text.toString(),
            vendorName = vendor.text.toString(),
            price = price.text.toString().toDouble(),
            weight = weight.text.toString(),
        )
        ref.setValue(product).addOnSuccessListener {
            Toast.makeText(applicationContext, "Product created", Toast.LENGTH_SHORT).show()
            progressBar.isVisible = false
            finish()
        }


    }

    private fun initValues(productModel: ProductModel) {
        Picasso.get().load(productModel.productImg).into(imageView)
        vendor.setText(productModel.vendorName)
        name.setText(productModel.productName)
        price.setText(productModel.price.toString())
        weight.setText(productModel.weight)
    }



    private fun init() {
        imageView = findViewById(R.id.imageView3)
        imageButton = findViewById(R.id.imageButton3)
//        spinner = findViewById(R.id.type_add)
        name = findViewById(R.id.editTextTextPersonName5)
        vendor = findViewById(R.id.edtVendor)
        price = findViewById(R.id.editTextTextPersonName6)
        weight = findViewById(R.id.editTextTextPersonName7)
        button = findViewById(R.id.button7)
        progressBar = findViewById(R.id.progressBar)
        progressBar.isVisible = false
    }
}
//private fun initSpinner() {
  //  typesArrayList.add(Constants.FishVendor)
    //typesArrayList.add(Constants.PorkBeefVendor)
    //typesArrayList.add(Constants.ChickenVendor)
    //typesArrayList.add(Constants.VegetableVendor)
    //typesArrayList.add(Constants.GroceryVendor)


    //Add types to spinner
    //val arrayAdapter: ArrayAdapter<String> =
      //  ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typesArrayList)
    //spinner.adapter = arrayAdapter


//}