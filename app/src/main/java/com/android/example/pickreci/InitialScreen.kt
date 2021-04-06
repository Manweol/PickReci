package com.android.example.pickreci

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class InitialScreen : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var registerBtn: Button
    private lateinit var loginBtn: Button
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_screen)
        init()
        initListeners()
        checkIfAnAccountIsLoggedIn()


    }
    private fun checkIfAnAccountIsLoggedIn() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            //do nothing
        } else {
            // if the user is logged in
            updateUI(user)
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            if (currentUser.isEmailVerified) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                Toast.makeText(baseContext, "Signed in",
                    Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(baseContext, "Please verify your email address.",
                    Toast.LENGTH_LONG).show()
                FirebaseAuth.getInstance().signOut()
            }
        } else {
            Toast.makeText(baseContext, "Incorrect email or password.",
                Toast.LENGTH_LONG).show()
        }
    }

    private fun initListeners() {
        //register listener
        registerBtn.setOnClickListener {
            startActivity(Intent(this, RegistrationScreen::class.java ))
        }
        //login listemer
        loginBtn.setOnClickListener {
            login()
        }


    }

    private fun login() {
        if (email.text.toString().isEmpty()) {
            email.error = "Please enter email"
            email.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.error = "Please enter valid email"
            email.requestFocus()
            return
        }

        if (password.text.toString().isEmpty()) {
            password.error = "Please enter password"
            password.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    updateUI(null)
                }
            }
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        email = findViewById(R.id.editTextTextEmailAddress)
        password = findViewById(R.id.editTextTextPassword)
        registerBtn = findViewById(R.id.button3)
        loginBtn = findViewById(R.id.button2)
    }
}