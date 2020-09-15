package com.miniproject.bookapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import org.w3c.dom.Text

class SplashActivity : AppCompatActivity() {

    lateinit var logo: Animation
    lateinit var name: Animation
    lateinit var imgLogo: ImageView
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        imgLogo = findViewById(R.id.imgLogo)
        textView = findViewById(R.id.textView)

        logo = AnimationUtils.loadAnimation(this, R.anim.logo)
        name = AnimationUtils.loadAnimation(this, R.anim.name)

        imgLogo.startAnimation(logo)
        textView.startAnimation(name)

        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 2200)
    }
}