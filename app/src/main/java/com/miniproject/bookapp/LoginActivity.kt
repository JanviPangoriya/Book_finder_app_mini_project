package com.miniproject.bookapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    lateinit var register: TextView
    lateinit var txtForgetPassword: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        register = findViewById(R.id.register)
        txtForgetPassword = findViewById(R.id.txtForgetPassword)
        register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
        txtForgetPassword.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
            finish()
        }
    }
}