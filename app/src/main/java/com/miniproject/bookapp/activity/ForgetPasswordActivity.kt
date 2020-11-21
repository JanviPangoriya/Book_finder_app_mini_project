package com.miniproject.bookapp.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.miniproject.bookapp.R
import com.miniproject.bookapp.util.ConnectionManager

class ForgetPasswordActivity : AppCompatActivity() {

    lateinit var edtforgetemail: EditText
    lateinit var btnGetEmail: Button
    lateinit var progressBar: ProgressBar
    lateinit var fauth: FirebaseAuth
    lateinit var password: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        init()

        progressBar.visibility = View.GONE

        password.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnGetEmail.setOnClickListener {

            val email = edtforgetemail.text.toString()

            if (email.isNotEmpty()) {

                progressBar.visibility = View.GONE

                if (ConnectionManager().checkConnectivity(this@ForgetPasswordActivity)) {

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
                    val dialog = AlertDialog.Builder(
                        this@ForgetPasswordActivity
                    )
                    dialog.setTitle("Error")
                    dialog.setMessage("Internet Connection is not Found")
                    dialog.setPositiveButton("Open Settings") { text, listener ->
                        val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingsIntent)
                        finish()
                    }
                    dialog.setNegativeButton("Exit") { text, listener ->
                        ActivityCompat.finishAffinity(this@ForgetPasswordActivity)
                    }
                    dialog.create()
                    dialog.show()
                }

            } else {
                edtforgetemail.error = "Please fill out this field"
            }

        }
    }

    private fun init() {
        edtforgetemail = findViewById(R.id.edtforgetemail)
        btnGetEmail = findViewById(R.id.btnGetEmail)
        password = findViewById(R.id.password)
        progressBar = findViewById(R.id.progressBar)
        fauth = FirebaseAuth.getInstance()
    }

    /*override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }*/
}