package com.miniproject.bookapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.miniproject.bookapp.R


class RegisterActivity : AppCompatActivity() {

    lateinit var edtname: EditText
    lateinit var edtemail: EditText
    lateinit var edtphone: EditText
    lateinit var edtpassword: TextInputEditText
    lateinit var edtconfirmpassword: TextInputEditText
    lateinit var btnCreateAcc: Button
    lateinit var progressBar: ProgressBar
    lateinit var txtMember: TextView
    lateinit var fauth: FirebaseAuth
    lateinit var fstore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        edtname = findViewById(R.id.edtname)
        edtemail = findViewById(R.id.edtemail)
        edtphone = findViewById(R.id.edtphone)
        edtpassword = findViewById(R.id.edtpassword)
        edtconfirmpassword = findViewById(R.id.edtconfirmpassword)
        btnCreateAcc = findViewById(R.id.btnCreateAcc)
        progressBar = findViewById(R.id.progressBar)
        txtMember = findViewById(R.id.txtMember)
        fauth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()

        progressBar.visibility = View.GONE

        btnCreateAcc.setOnClickListener {

            val name = edtname.text.toString()
            val email = edtemail.text.toString()
            val phoneno = edtphone.text.toString()
            val password = edtpassword.text.toString()
            val confirmpassword = edtconfirmpassword.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && phoneno.isNotEmpty() && password.isNotEmpty() && confirmpassword.isNotEmpty()) {
                if (password.equals(confirmpassword) && phoneno.length == 10) {
                    progressBar.visibility = View.VISIBLE
                    fauth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = User(name, email, phoneno, password)
                                val userid = fauth.currentUser?.uid
                                if (userid != null) {
                                    fstore.collection("users").document(userid).set(user)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                startActivity(
                                                    Intent(
                                                        this,
                                                        HomeActivity::class.java
                                                    )
                                                )
                                                finish()
                                                Toast.makeText(
                                                    this,
                                                    "Registered Successfully",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                } else {
                                    Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT)
                                        .show()

                                }

                            } else {
                                progressBar.visibility = View.GONE
                                Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT)
                                    .show()

                            }

                        }
                } else {
                    if (phoneno.length != 10) {
                        edtphone.error = "Invalid"
                        Toast.makeText(
                            this,
                            "Invalid Phone No",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Password and confirm password do not match",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            } else {
                if (name.isEmpty()) {
                    edtname.error = "Please fill out this field"
                }
                if (email.isEmpty()) {
                    edtemail.error = "Please fill out this field"
                }
                if (phoneno.isEmpty()) {
                    edtphone.error = "Please fill out this field"
                }
                if (password.isEmpty()) {
                    edtpassword.error = "Please fill out this field"
                }
                if (confirmpassword.isEmpty()) {
                    edtconfirmpassword.error = "Please fill out this field"
                }
            }
        }
        txtMember.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}