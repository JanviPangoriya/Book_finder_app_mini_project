package com.miniproject.bookapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    lateinit var btnCreateAcc: Button
    lateinit var txtMember: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        btnCreateAcc = findViewById(R.id.btnCreateAcc)
        txtMember = findViewById(R.id.txtMember)
        btnCreateAcc.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        txtMember.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}