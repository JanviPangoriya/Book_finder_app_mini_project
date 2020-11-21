package com.miniproject.bookapp.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miniproject.bookapp.R
import com.miniproject.bookapp.activity.BookListActivity
import com.miniproject.bookapp.adapter.DashboardRecyclerAdapter

class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: DashboardRecyclerAdapter
    lateinit var edtSearch: EditText
    lateinit var btnSearch: Button

    var genreCategoryList = arrayListOf(
        "Fiction",
        "Education",
        "Thriller",
        "Romance",
        "Mystery",
        "Biography",
        "Fantasy",
        "Novel"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        edtSearch = view.findViewById(R.id.edtSearch)
        btnSearch = view.findViewById(R.id.btnSearch)

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard1)
        layoutManager = LinearLayoutManager(activity)

        btnSearch.setOnClickListener {
            val searchTerm = edtSearch.text.toString()
            if (searchTerm.isNotEmpty()) {
                val intent = Intent(activity, BookListActivity::class.java)
                intent.putExtra("searchTerm", edtSearch.getText().toString())
                activity?.startActivity(intent)
            } else {
                Toast.makeText(activity, "Please Enter Your Query !", Toast.LENGTH_LONG).show()
            }
        }
        recyclerDashboard.setHasFixedSize(true)
        recyclerAdapter = DashboardRecyclerAdapter(activity as Context, genreCategoryList)
        recyclerDashboard.adapter = recyclerAdapter
        recyclerDashboard.layoutManager = layoutManager

        return view
    }
}