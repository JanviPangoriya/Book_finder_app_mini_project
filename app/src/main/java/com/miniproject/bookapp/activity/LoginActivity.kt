package com.miniproject.bookapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.miniproject.bookapp.R

class LoginActivity : AppCompatActivity() {
    lateinit var etEmail: EditText
    lateinit var etPassword: TextInputEditText
    lateinit var btnLogin: Button
    lateinit var progressBar: ProgressBar
    lateinit var txtForgetPassword: TextView
    lateinit var register: TextView
    lateinit var fauth: FirebaseAuth
    lateinit var fstore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()

        if (fauth.currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        progressBar.visibility = View.GONE

        btnLogin.setOnClickListener {

            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                progressBar.visibility = View.VISIBLE
                fauth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userid = fauth.currentUser?.uid
                        if (userid != null) {
                            fstore.collection("users").document(userid).update("password", password)
                        }
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    } else {

                        progressBar.visibility = View.GONE
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                showError(etEmail)
                showError(etPassword)
            }
        }

        register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
        txtForgetPassword.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
            finish()
        }
    }

    private fun init() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar)
        txtForgetPassword = findViewById(R.id.txtForgetPassword)
        register = findViewById(R.id.register)
        fauth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
    }

    private fun showError(inputField: EditText) {
        if (inputField.text.isEmpty()) {
            inputField.error = "Please fill out this field"
        }
    }
}