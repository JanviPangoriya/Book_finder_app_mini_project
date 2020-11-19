package com.miniproject.bookapp.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.miniproject.bookapp.R
import com.miniproject.bookapp.model.Book
import com.miniproject.bookapp.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject

class BookDescriptionActivity : AppCompatActivity() {

    lateinit var imgBookImage: ImageView
    lateinit var txtAuthorname: TextView
    lateinit var txtSubtitle: TextView
    lateinit var txtTitle: TextView
    lateinit var txtPagecount: TextView
    lateinit var txtLanguage: TextView
    lateinit var txtAboutbook: TextView
    lateinit var txtPublishername: TextView
    lateinit var txtPublishedate: TextView
    lateinit var toolbar: Toolbar
    lateinit var queue: RequestQueue
    lateinit var jsonObject: JSONObject
    lateinit var book: Book
    lateinit var llBuy: LinearLayout
    lateinit var buyView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_description)
        init()
        toolbar.title = "Description"
        queue = Volley.newRequestQueue(this)
        val url = intent.getStringExtra("selfLink")
        jsonObject = JSONObject()
        if (ConnectionManager().checkConnectivity(this)) {

            val uri = Uri.parse(url)
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

    fun init() {
        imgBookImage = findViewById(R.id.imgBookImage)
        txtAuthorname = findViewById(R.id.txtAuthorname)
        txtSubtitle = findViewById(R.id.txtSubtitle)
        txtTitle = findViewById(R.id.txtTitle)
        txtPagecount = findViewById(R.id.txtPagecount)
        txtLanguage = findViewById(R.id.txtLanguage)
        txtAboutbook = findViewById(R.id.txtAboutbook)
        txtPublishername = findViewById(R.id.txtPublishername)
        txtPublishedate = findViewById(R.id.txtPublishedate)
        toolbar = findViewById(R.id.toolbar)
        llBuy = findViewById(R.id.llBuy)
        buyView = findViewById(R.id.buyView)
    }

    fun parseJson(key: String) {
        val request = object :
            JsonObjectRequest(Method.GET, key, null, Response.Listener {
                var title = ""
                var subtitle = ""
                var author = "NOT AVAILABLE"
                var thumbnail = "NOT AVAILABLE"
                var publisher = "NOT AVAILABLE"
                var publishedDate = "NOT AVAILABLE"
                var description = "NO DESCRIPTION"
                var pageCount = 1000
                var rating = 3.0
                var price = "NOT FOR SALE"
                var buyLink = "NOT FOR SALE"
                try {
                    val volumeInfo = it.getJSONObject("volumeInfo")
                    title = volumeInfo.getString("title")
                    if (volumeInfo.has("subtitle")) {
                        subtitle = volumeInfo.getString("subtitle")
                    }
                    if (volumeInfo.has("authors")) {
                        val authors = volumeInfo.getJSONArray("authors")
                        if (authors.length() == 1) {
                            author = authors.getString(0)
                        } else {
                            author = authors.getString(0) + "\n" + authors.getString(1);
                        }
                    }
                    if (volumeInfo.has("publisher")) {
                        publisher = volumeInfo.getString("publisher")
                    }
                    if (volumeInfo.has("publishedDate")) {
                        publishedDate = volumeInfo.getString("publishedDate")
                    }
                    if (volumeInfo.has("desciption")) {
                        description = volumeInfo.getString("description")
                    }
                    if (volumeInfo.has("pageCount")) {
                        pageCount = volumeInfo.getInt("pageCount")
                    }
                    if (volumeInfo.has("averageRating")) {
                        rating = volumeInfo.getDouble("averageRating")
                    }

                    if (volumeInfo.has("imageLinks")) {
                        thumbnail = volumeInfo.getJSONObject("imageLinks").getString("thumbnail")
                    }

                    val language = volumeInfo.getString("language")

                    val previewLink = volumeInfo.getString("previewLink")

                    val infoLink = volumeInfo.getString("infoLink")

                    val saleInfo = it.getJSONObject("saleInfo")

                    if (saleInfo.has("listPrice")) {
                        llBuy.visibility = View.VISIBLE
                        buyView.visibility = View.VISIBLE
                        val listPrice = saleInfo.getJSONObject("listPrice")
                        price =
                            listPrice.getString("amount") + " " + listPrice.getString("currencyCode")
                        buyLink = saleInfo.getString("buyLink")
                    }

                    book = Book(
                        title,
                        subtitle,
                        author,
                        publisher,
                        publishedDate,
                        description,
                        pageCount,
                        rating,
                        thumbnail,
                        language,
                        previewLink,
                        price,
                        buyLink,
                        infoLink
                    )

                    updateLayout()

                } catch (e: JSONException) {
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }

            }, Response.ErrorListener {
                Toast.makeText(
                    this,
                    "Volley error occurred!",
                    Toast.LENGTH_SHORT
                ).show()
            }) {}
        queue.add(request)
    }

    fun updateLayout() {
        if (!book.thumbnail.equals("NOT AVAILABLE")) {
            Picasso.get().load(book.thumbnail).error(R.drawable.logo).into(imgBookImage)
        }
        txtAuthorname.text = book.author
        txtSubtitle.text = book.subtitle
        txtTitle.text = book.title
        txtPagecount.text = book.pageCount.toString()
        txtLanguage.text = book.language
        txtAboutbook.text = book.description
        txtPublishername.text = book.publisher
        txtPublishedate.text = book.publishedDate

    }

    fun WebBuy(view:View){
        openUrl(book.buyLink)
    }

    fun WebInfo(view:View){
        openUrl(book.infoLink)
    }

    fun WebPreview(view:View){
        openUrl(book.previewLink)
    }

    fun openUrl(url:String){
        var uri: Uri = Uri.parse(url)
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}