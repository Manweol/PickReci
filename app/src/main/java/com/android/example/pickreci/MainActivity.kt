package com.android.example.pickreci

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.example.pickreci.InitialScreen.InitialScreen
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


  class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {





    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    private lateinit var appBarConfiguration: AppBarConfiguration

    companion object {
        const val TAG = "MainActivityTag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkIfLoggedIn()

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)

        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
           R.id.nav_home, R.id.nav_myaccount,  R.id.nav_recipe, R.id.nav_orders, R.id.nav_cart, R.id.nav_aboutus), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        //Signout listener
        navView.menu.findItem(R.id.logout).setOnMenuItemClickListener { _ ->
            showDialog()
            true
        }

//splash screen

        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()

    }

    private fun checkIfLoggedIn() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
           val intent = Intent(this, InitialScreen::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_home -> {
                Toast.makeText(this, "Home Clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_myaccount -> {
                Toast.makeText(this, "My Account Clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_recipe -> {
                Toast.makeText(this, "Recipe Clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_orders -> {
                Toast.makeText(this, "Orders Clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_cart -> {
                Toast.makeText(this, "Cart Clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_aboutus -> {
                Toast.makeText(this, "About Us Clicked", Toast.LENGTH_SHORT).show()
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Do you want to sign out now?")
            .setCancelable(true)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
               FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, InitialScreen::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                Toast.makeText(baseContext, "Signed out",
                    Toast.LENGTH_LONG).show()


            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Sign out")
        alert.show()



    }

}

