package com.miniproject.bookapp.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.miniproject.bookapp.R
import com.miniproject.bookapp.model.User
import com.miniproject.bookapp.util.ConnectionManager


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

        init()

        progressBar.visibility = View.GONE

        btnCreateAcc.setOnClickListener {

            val name = edtname.text.toString()
            val email = edtemail.text.toString()
            val phoneno = edtphone.text.toString()
            val password = edtpassword.text.toString()
            val confirmpassword = edtconfirmpassword.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && phoneno.isNotEmpty() && password.isNotEmpty() && confirmpassword.isNotEmpty()) {
                if (password.equals(confirmpassword) && phoneno.length == 10 && Patterns.PHONE.matcher(phoneno).matches()) {
                    if (ConnectionManager().checkConnectivity(this@RegisterActivity)) {

                        progressBar.visibility = View.VISIBLE
                        fauth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = User(
                                        name,
                                        email,
                                        phoneno,
                                        password
                                    )
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
                                        Toast.makeText(
                                            this,
                                            "Some error occurred",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }

                                } else {
                                    progressBar.visibility = View.GONE
                                    Toast.makeText(
                                        this,
                                        task.exception?.message,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }
                    } else {
                        val dialog = AlertDialog.Builder(
                            this@RegisterActivity
                        )
                        dialog.setTitle("Error")
                        dialog.setMessage("Internet Connection is not Found")
                        dialog.setPositiveButton("Open Settings") { text, listener ->
                            val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                            startActivity(settingsIntent)
                            finish()
                        }
                        dialog.setNegativeButton("Exit") { text, listener ->
                            ActivityCompat.finishAffinity(this@RegisterActivity)
                        }
                        dialog.create()
                        dialog.show()
                    }
                } else {
                    if (phoneno.length != 10 || !Patterns.PHONE.matcher(phoneno).matches()) {
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
                showError(edtname)
                showError(edtemail)
                showError(edtphone)
                showError(edtpassword)
                showError(edtconfirmpassword)
            }
        }
        txtMember.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun init() {
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
    }

    private fun showError(inputField: EditText) {
        if (inputField.text.isEmpty()) {
            inputField.error = "Please fill out this field"
        }
    }

    /*override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }*/
}