package com.miniproject.bookapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class HomeActivity : AppCompatActivity() {

    lateinit var name: TextView
    lateinit var email: TextView
    lateinit var phoneno: TextView
    lateinit var password: TextView
    lateinit var btnSignOut: Button
    lateinit var fauth: FirebaseAuth
    lateinit var fstore: FirebaseFirestore
    lateinit var progressBar: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        phoneno = findViewById(R.id.phoneno)
        password = findViewById(R.id.password)
        btnSignOut = findViewById(R.id.btnSignOut)
        fauth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        progressBar = findViewById(R.id.progressBar)

        progressBar.visibility = View.VISIBLE

        val uid = fauth.currentUser?.uid

        if (uid != null) {
            fstore.collection("users").document(uid).get()
                .addOnSuccessListener { documentSnapshot ->

                    val data = documentSnapshot.data
                    if (data != null) {
                        progressBar.visibility = View.GONE
                        name.text = "NAME: " + data["name"]
                        email.text = "EMAIL ID: " + fauth.currentUser?.email
                        phoneno.text = "PHONE NO: " + data["phoneNo"]
                        password.text = "PASSWORD: " + data["password"]
                    }

                }
        }

        btnSignOut.setOnClickListener {
            fauth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}