package com.miniproject.bookapp.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.miniproject.bookapp.R
import com.miniproject.bookapp.adapter.BookListAdapter
import com.miniproject.bookapp.model.Book
import com.miniproject.bookapp.model.DashboardChildItem
import com.miniproject.bookapp.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class BookListActivity : AppCompatActivity() {
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var queue: RequestQueue
    lateinit var jsonObject: JSONObject
    lateinit var recyclerview: RecyclerView
    lateinit var adapter: BookListAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    var books = arrayListOf<DashboardChildItem>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        toolbar = findViewById(R.id.toolbar)
        recyclerview = findViewById(R.id.recyclerview)
        layoutManager = LinearLayoutManager(this)

        setUpToolbar()

        recyclerview.setHasFixedSize(true)

        books.clear()

        queue = Volley.newRequestQueue(this)
        val BASE_URL = "https://www.googleapis.com/books/v1/volumes?q="
        jsonObject = JSONObject()
        val search_query = intent.getStringExtra("searchTerm")

        if (ConnectionManager().checkConnectivity(this)) {

            val final_query = search_query.replace(" ", "+")
            val uri = Uri.parse(BASE_URL + final_query);
            val buider = uri.buildUpon()
            parseJson(buider.toString())

        } else {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("open setting")
            { text, listner ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("exit")
            { text, listner ->
                ActivityCompat.finishAffinity(this)
            }

            dialog.create()
            dialog.show()
        }
    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Books"
    }

    fun parseJson(key: String) {
        val request = object : JsonObjectRequest(Method.GET, key, null, Response.Listener {
            var title = ""
            var author = "NOT AVAILABLE"
            var thumbnail = "NOT AVAILABLE"
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
                    books.add(
                        DashboardChildItem(
                            thumbnail,
                            title,
                            author,
                            selfLink
                        )
                    )
                    adapter = BookListAdapter(this, books)
                    recyclerview.adapter = adapter
                    recyclerview.layoutManager = layoutManager
                }
            } catch (e: JSONException) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener {
            Toast.makeText(
                this,
                "Volley error occurred!",
                Toast.LENGTH_SHORT
            ).show()

        }) {
        }
        queue.add(request)
    }
}
