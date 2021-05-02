package com.android.example.pickreci.MyAccount

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.android.example.pickreci.MainActivity.Companion.TAG
import com.android.example.pickreci.Models.User
import com.android.example.pickreci.R
import com.android.example.pickreci.RegistrationScreen.RegistrationScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.*



class MyAccount : Fragment() {
    private lateinit var image : ImageView
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var address: EditText
    private lateinit var number: EditText
    private lateinit var btn: Button
    private lateinit var v: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_my_account, container, false)
        init()
        fetchUserData()
        initListeners()

        return v
    }

    private fun fetchUserData() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser.uid
        val ref = FirebaseDatabase.getInstance().getReference("users/${currentUserUid}")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentUserData = snapshot.getValue(User::class.java)
                name.setText(currentUserData!!.name)
                email.setText(currentUserData!!.email)
                number.setText(currentUserData!!.number.toString())
                address.setText(currentUserData!!.address)
                Picasso.get().load(currentUserData.imageURL).into(image)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun initListeners() {
        //Button listemer
        btn.setOnClickListener {
            when(btn.text.toString()){
                UPDATE_TEXT -> {
                    changeIsEnabledAttribute(UPDATE_TEXT)
                    Toast.makeText(v.context, "You can now update.", Toast.LENGTH_SHORT).show()

                }
                SAVE_TEXT -> {
                    executeUpdate()
                    changeIsEnabledAttribute(SAVE_TEXT)

                }
            }
        }
        //Image view listener
        image.setOnClickListener {
            chooseImage()
        }
    }

    private fun executeUpdate() {
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
            val currentUserUid = FirebaseAuth.getInstance().currentUser.uid
            val ref = FirebaseDatabase.getInstance().getReference("users/$currentUserUid")
            ref.child("name").setValue(name.text.toString())
            ref.child("address").setValue(address.text.toString().toInt())
            ref.child("number").setValue(number.text.toString().toInt())
            ref.child("email").setValue(email.text.toString())
            Toast.makeText(v.context, "Account successfuly updated.", Toast.LENGTH_SHORT).show()

        }


    }

    private fun saveToDatabase(url: String) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser.uid
        val ref = FirebaseDatabase.getInstance().getReference("users/$currentUserUid")
        ref.child("imageURL").setValue(url)
        ref.child("name").setValue(name.text.toString())
        ref.child("address").setValue(address.text.toString().toInt())
        ref.child("number").setValue(number.text.toString().toInt())
        ref.child("email").setValue(email.text.toString())
        Toast.makeText(v.context, "Account successfully updated.", Toast.LENGTH_SHORT).show()
    }

    private fun changeIsEnabledAttribute(text: String) {
        if (text == UPDATE_TEXT) {
            btn.text = SAVE_TEXT
            image.isEnabled = true
            name.isEnabled = true
            address.isEnabled = true
            number.isEnabled = true
        } else {
            btn.text = UPDATE_TEXT
            image.isEnabled = false
            name.isEnabled = false
            address.isEnabled = false
            number.isEnabled = false
        }
    }

    private fun init() {
        image = v.findViewById(R.id.image_profile)
        name = v.findViewById(R.id.name_profile)
        email = v.findViewById(R.id.email_profile)
        address = v.findViewById(R.id.address_profile)
        number = v.findViewById(R.id.number_profile)
        btn = v.findViewById(R.id.btn_profile)
        btn.text = UPDATE_TEXT

        email.isEnabled = false

        image.isEnabled = false
        name.isEnabled = false
        address.isEnabled = false
        number.isEnabled = false
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeType = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(intent, RegistrationScreen.IMAGE_PICK_CODE)
    }
    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            RegistrationScreen.IMAGE_PICK_CODE -> {
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



    companion object {
        const val UPDATE_TEXT = "UPDATE"
        const val SAVE_TEXT = "SAVE"
    }
}

