package com.miniproject.bookapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miniproject.bookapp.R

class recyclerview : AppCompatActivity() {
    lateinit var recyclerview: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.recyclerview)
        recyclerview = findViewById(R.id.recyclerview)
        layoutManager = LinearLayoutManager(this)

    }
}