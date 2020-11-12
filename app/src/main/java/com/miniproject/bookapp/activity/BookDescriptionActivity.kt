package com.miniproject.bookapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.miniproject.bookapp.R

class BookDescriptionActivity : AppCompatActivity() {
    //lateinit var textView1:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_description)
        /*textView1=findViewById(R.id.textView1)
        if(intent!=null)
        {
            textView1.text=intent.getStringExtra("selfLink")
        }*/
    }
}