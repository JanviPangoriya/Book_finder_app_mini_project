package com.miniproject.bookapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.miniproject.bookapp.R

class ForgetPasswordActivity : AppCompatActivity() {

    lateinit var edtforgetemail: EditText
    lateinit var btnGetEmail: Button
    lateinit var progressBar: ProgressBar
    lateinit var fauth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        init()

        progressBar.visibility = View.GONE

        btnGetEmail.setOnClickListener {

            val email = edtforgetemail.text.toString()

            if (email.isNotEmpty()) {

                progressBar.visibility = View.GONE
                fauth.sendPasswordResetEmail(email).addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        startActivity(Intent(this, LoginActivity::class.java))
                        Toast.makeText(this, "Email Sent!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                edtforgetemail.error = "Please fill out this field"
            }

        }
    }

    private fun init() {
        edtforgetemail = findViewById(R.id.edtforgetemail)
        btnGetEmail = findViewById(R.id.btnGetEmail)
        progressBar = findViewById(R.id.progressBar)
        fauth = FirebaseAuth.getInstance()
    }
}