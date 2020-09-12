package com.miniproject.bookapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ForgetPasswordActivity : AppCompatActivity() {
    lateinit var btnGetEmail: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        btnGetEmail = findViewById(R.id.btnGetEmail)
        btnGetEmail.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            Toast.makeText(this, "Email Sent!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}