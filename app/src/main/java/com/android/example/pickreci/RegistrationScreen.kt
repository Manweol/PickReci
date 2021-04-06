package com.android.example.pickreci

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.android.example.pickreci.Models.User
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RegistrationScreen : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var age: EditText
    private lateinit var number: EditText
    private lateinit var password: EditText
    private lateinit var retypePassword: EditText
    private lateinit var registerBtn: Button
    private lateinit var image: ImageView
    private lateinit var backBtn : ImageButton
    private val auth = FirebaseAuth.getInstance()
    private var user: FirebaseUser?= null

    companion object {
        const val IMAGE_PICK_CODE = 1001
        const val DEFAULT_IMG_URL = "https://firebasestorage.googleapis.com/v0/b/recipick-9e847.appspot.com/o/default-images%2Fmain-qimg-2b21b9dd05c757fe30231fac65b504dd.png?alt=media&token=6acd4366-8d35-4769-82a3-2759623a9214"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_screen)
        init()
        initListeners()
    }

    private fun initListeners() {
        //button listener
        registerBtn.setOnClickListener {
            createAccount()
        }
        //image listener
        image.setOnClickListener {
            chooseImage()
        }
        //back button
        backBtn.setOnClickListener {
            finish()
        }

    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeType = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }
    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            IMAGE_PICK_CODE -> {
                if (resultCode == Activity.RESULT_OK){
                   data!!.data.let { uri ->
                       //set it as the image
                       image.setImageURI(uri)
                       selectedPhotoUri = uri

                   }
                } else {

                }
            }
        }
    }

    private fun createAccount() {
        //Check entries
        if (name.text.toString().isEmpty()){
            name.error = "Enter name"
            name.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            email.error = "Invalid email"
            email.requestFocus()
            return
        }
        if (age.text.toString().isEmpty()) {
            age.error = "Enter age"
            age.requestFocus()
            return
        }
        if(number.text.toString().isEmpty()){
            number.error = "Enter number"
            number.requestFocus()
            return
        }
        if(password.text.toString().isEmpty()){
            password.error= "Enter password"
            password.requestFocus()
            return
        }
        if(retypePassword.text.toString() != password.text.toString()){
            retypePassword.error = "Password doesn't match"
            retypePassword.requestFocus()
            return
        }
        //Create account
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Do you want to create this account? After creating account, verify it through your email address.")
            .setCancelable(true)
            .setPositiveButton("Create Account", DialogInterface.OnClickListener { _, _ ->
                auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            //Email Verification
                            user = auth.currentUser
                            user!!.sendEmailVerification()
                                .addOnCompleteListener { emailTask ->
                                    if (emailTask.isSuccessful) {
                                        //Upload the image to firebase database, inside this function, the save user to firebase database will also be called.
                                        uploadImage()
                                    }
                                }
                        } else {
                            try {
                                val text = task.result.toString()
                            } catch (e: RuntimeExecutionException) {
                                val message = e.message!!
                                Toast.makeText(
                                    this,
                                    message.substringAfter(':', "Try again later."),
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                        }
                    }
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Create account")
        alert.show()

    }

    private fun uploadImage() {
        if (selectedPhotoUri != null) {
            val fileName = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/profile-images/$fileName")
            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { url ->
                        saveToDatabase(url.toString())
                    }
                }

        } else {
            saveToDatabase(DEFAULT_IMG_URL)
        }
    }

    private fun saveToDatabase(url: String) {
        val ref = FirebaseDatabase.getInstance().getReference("users/${user!!.uid}")
        val userModel = User(
            uid = user!!.uid,
            imageURL = url,
            age = age.text.toString().toInt(),
            email = email.text.toString(),
            number = number.text.toString().toInt(),
            name = name.text.toString()
        )
        ref.setValue(userModel)

        finish()
        Toast.makeText(this, "Account successfuly created.", Toast.LENGTH_SHORT).show()
    }


    private fun init() {
        backBtn = findViewById(R.id.imageButton2)
        image = findViewById(R.id.imageView)
        name = findViewById(R.id.editTextTextPersonName)
        email= findViewById(R.id.editTextTextPersonName2)
        age= findViewById(R.id.editTextTextPersonName3)
        number= findViewById(R.id.editTextTextPersonName4)
        password= findViewById(R.id.textView)
        retypePassword= findViewById(R.id.textView3)
        registerBtn= findViewById(R.id.button4)
    }
}