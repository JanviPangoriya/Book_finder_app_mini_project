package com.miniproject.bookapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    lateinit var btnSignOut: Button
    lateinit var fauth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        btnSignOut = findViewById(R.id.btnSignOut)
        btnSignOut.setOnClickListener {
            fauth = FirebaseAuth.getInstance()
            fauth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}