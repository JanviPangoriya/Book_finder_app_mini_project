package com.miniproject.bookapp.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.miniproject.bookapp.R
import com.miniproject.bookapp.activity.BookListActivity
import com.miniproject.bookapp.adapter.DashboardRecyclerAdapter
import com.miniproject.bookapp.model.DashboardChildItem
import com.miniproject.bookapp.model.DashboardParentItem
import org.json.JSONException

class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: DashboardRecyclerAdapter
    lateinit var edtSearch: EditText
    lateinit var btnSearch: Button
    lateinit var queue: RequestQueue
    var itemList = arrayListOf<DashboardParentItem>()

    var genreCategoryList = arrayListOf<String>("Fiction", "Education", "Thriller", "Romance", "Mystery", "Biography", "Fantasy", "Novel")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        edtSearch = view.findViewById(R.id.edtSearch)
        btnSearch = view.findViewById(R.id.btnSearch)

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

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard1)
        layoutManager = LinearLayoutManager(activity)

        for (i in 0 until genreCategoryList.size){
            itemList.add(DashboardParentItem(genreCategoryList[i], api(genreCategoryList[i])))
        }

        recyclerAdapter = DashboardRecyclerAdapter(activity as Context, itemList)
        recyclerDashboard.adapter = recyclerAdapter
        recyclerDashboard.layoutManager = layoutManager

        return view
    }

    private fun api(genreCategory: String): ArrayList<DashboardChildItem>{

        var childItemList = arrayListOf<DashboardChildItem>()

        childItemList.clear()

        queue = Volley.newRequestQueue(context)
        val BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=subject:${genreCategory}&oderBy=newest"
        val request = object : JsonObjectRequest(Method.GET, BASE_URL, null, Response.Listener {

            var title = ""
            var author = "NOT AVAILABLE"
            var thumbnail = ""
            var selfLink = ""

            try {
                val items = it.getJSONArray("items")
                for (i in 0 until items.length()) {
                    val item = items.getJSONObject(i)
                    val volumeInfo = item.getJSONObject("volumeInfo")
                    title = volumeInfo.getString("title")
                    selfLink = item.getString("selfLink")
                    if (volumeInfo.has("authors")) {
                        val authors = volumeInfo.getJSONArray("authors")
                        if (authors.length() == 1) {
                            author = authors.getString(0)
                        } else {
                            author = authors.getString(0) + "\n" + authors.getString(1);
                        }
                    }
                    if (volumeInfo.has("imageLinks")) {
                        thumbnail = volumeInfo.getJSONObject("imageLinks").getString("thumbnail")
                    }

                    childItemList.add(DashboardChildItem(
                        thumbnail,
                        title,
                        author,
                        selfLink
                    ))

                }
            }catch (e: JSONException) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }}, Response.ErrorListener {

            Toast.makeText(
                context,
                "Volley error occurred!",
                Toast.LENGTH_SHORT
            ).show()

        }) {
        }
        queue.add(request)

        return childItemList
    }
}