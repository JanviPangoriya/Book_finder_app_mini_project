package com.miniproject.bookapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.miniproject.bookapp.R

class HomeActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    var previousMenuItem: MenuItem? = null
    lateinit var fauth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        init()

        setUpToolbar()

        var actionBarDrawerToggle = ActionBarDrawerToggle(
            this@HomeActivity, drawerLayout, R.string.open_drawer, R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.title = "Dashboard"
        navigationView.setCheckedItem(R.id.dashboard)

        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }

            it.isChecked = true
            it.isCheckable = true

            previousMenuItem = it

            when (it.itemId) {
                R.id.dashboard -> {
                    Toast.makeText(
                        this@HomeActivity,
                        "Clicked on Dashboard",
                        Toast.LENGTH_SHORT
                    ).show()
                    supportActionBar?.title = "Dashboard"
                }
                R.id.profile -> {
                    Toast.makeText(
                        this@HomeActivity,
                        "Clicked on Profile",
                        Toast.LENGTH_SHORT
                    ).show()
                    supportActionBar?.title = "Profile"
                }
                R.id.faqs -> {
                    Toast.makeText(
                        this@HomeActivity,
                        "Clicked on FAQS",
                        Toast.LENGTH_SHORT
                    ).show()
                    supportActionBar?.title = "FAQS"
                }
                R.id.favourites -> {
                    Toast.makeText(
                        this@HomeActivity,
                        "Clicked on Favourites",
                        Toast.LENGTH_SHORT
                    ).show()
                    supportActionBar?.title = "Favourites"
                }
                R.id.signout -> signout()
                R.id.aboutus -> {
                    Toast.makeText(
                        this@HomeActivity,
                        "Clicked on About Us",
                        Toast.LENGTH_SHORT
                    ).show()
                    supportActionBar?.title = "About Us"
                }
            }
            return@setNavigationItemSelectedListener true
        }

    }

    private fun init() {
        drawerLayout = findViewById(R.id.drawer_layout)
        coordinatorLayout = findViewById(R.id.coordinator_layout)
        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.navigation_view)
        frameLayout = findViewById(R.id.frame_layout)
    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "BOOKOPEDIA"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun signout() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Confirmation")
        alertDialog.setMessage("Are you sure you want to signout?")
        alertDialog.setPositiveButton("Yes") { text, listner ->
            fauth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        alertDialog.setNegativeButton("No")
        { text, listner ->
            text.dismiss()
        }
        alertDialog.create()
        alertDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
}

/*    lateinit var name: TextView
    lateinit var email: TextView
    lateinit var phoneno: TextView
    lateinit var password: TextView
    lateinit var btnSignOut: Button
    lateinit var fauth: FirebaseAuth
    lateinit var fstore: FirebaseFirestore
    lateinit var progressBar: RelativeLayout*/
/*
        progressBar.visibility = View.VISIBLE

        val uid = fauth.currentUser?.uid

        if (uid != null) {

            fstore.collection("users").document(uid).get()
                .addOnSuccessListener { documentSnapshot ->

                    val data = documentSnapshot.data
                    if (data != null) {
                        progressBar.visibility = View.GONE
                        name.text = "NAME: " + data["name"]
                        email.text = "EMAIL ID: " + fauth.currentUser?.email
                        phoneno.text = "PHONE NO: " + data["phoneNo"]
                        password.text = "PASSWORD: " + data["password"]
                    }

                }
        }

        btnSignOut.setOnClickListener {
            fauth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun init() {
        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        phoneno = findViewById(R.id.phoneno)
        password = findViewById(R.id.password)
        btnSignOut = findViewById(R.id.btnSignOut)
        fauth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        progressBar = findViewById(R.id.progressBar)
    }*/

