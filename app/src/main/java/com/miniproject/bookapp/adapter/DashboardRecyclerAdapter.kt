package com.miniproject.bookapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.miniproject.bookapp.R
import com.miniproject.bookapp.activity.BookListActivity
import com.miniproject.bookapp.model.DashboardChildItem
import org.json.JSONException

class DashboardRecyclerAdapter(val context: Context, val itemItem: ArrayList<String>) :
    RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

    lateinit var queue: RequestQueue
    lateinit var childItemAdapter: DashboardChildItemRecyclerAdapter

    private var viewPool = RecyclerView.RecycledViewPool()

    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var categoryName: TextView = view.findViewById(R.id.categoryName)
        var childRecycler: RecyclerView = view.findViewById(R.id.childRecycler)
        var btnMore: Button = view.findViewById(R.id.btnMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_dashboard_parent, parent, false)
        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemItem.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val parentItem = itemItem[position]
        holder.categoryName.text = parentItem
        val layoutManager =
            LinearLayoutManager(holder.childRecycler.context, LinearLayoutManager.HORIZONTAL, false)
        val childItemList = arrayListOf<DashboardChildItem>()
        queue = Volley.newRequestQueue(context)
        val BASE_URL =
            "https://www.googleapis.com/books/v1/volumes?q=subject:${parentItem}&oderBy=newest"
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

                    childItemList.add(
                        DashboardChildItem(
                            thumbnail,
                            title,
                            author,
                            selfLink
                        )
                    )

                    childItemAdapter = DashboardChildItemRecyclerAdapter(context, childItemList)
                    holder.childRecycler.layoutManager = layoutManager
                    holder.childRecycler.adapter = childItemAdapter

                }
            } catch (e: JSONException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener {

            Toast.makeText(
                context,
                "Volley error occurred!",
                Toast.LENGTH_SHORT
            ).show()

        }) {
        }
        queue.add(request)
        holder.childRecycler.setRecycledViewPool(viewPool)
        holder.btnMore.setOnClickListener {
            val intent = Intent(context, BookListActivity::class.java)
            intent.putExtra("searchTerm", holder.categoryName.text.toString())
            context.startActivity(intent)
        }
    }
}