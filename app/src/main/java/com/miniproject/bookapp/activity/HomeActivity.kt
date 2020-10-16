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
import com.miniproject.bookapp.fragment.*

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

        openHome()

        var actionBarDrawerToggle = ActionBarDrawerToggle(
            this@HomeActivity, drawerLayout, R.string.open_drawer, R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }

            it.isChecked = true
            it.isCheckable = true

            previousMenuItem = it

            when (it.itemId) {
                R.id.dashboard -> {
                    openHome()
                    drawerLayout.closeDrawers()
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, ProfileFragment())
                        .commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "Profile"
                }
                R.id.faqs -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, FaqsFragment())
                        .commit()
                    supportActionBar?.title = "FAQS"
                    drawerLayout.closeDrawers()
                }
                R.id.favourites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, FavouritesFragment())
                        .commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "Favourites"
                }
                R.id.signout -> signout()
                R.id.aboutus -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout,AboutUsFragment())
                        .commit()
                    drawerLayout.closeDrawers()
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
        fauth = FirebaseAuth.getInstance()
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

    private fun openHome() {
        val fragment = DashboardFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.commit()
        navigationView.setCheckedItem(R.id.dashboard)
        supportActionBar?.title = "Dashboard"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame_layout)

        when (frag) {
            !is DashboardFragment -> openHome()
            else -> super.onBackPressed()
        }
    }
}