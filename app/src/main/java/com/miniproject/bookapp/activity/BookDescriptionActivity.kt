package com.miniproject.bookapp.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.view.View
import android.widget.*
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

    lateinit var txtTitle: TextView
    lateinit var txtSubtitle: TextView
    lateinit var txtAuthorName: TextView
    lateinit var imgBookImage: ImageView
    lateinit var setFav:TextView
    lateinit var removeFav:TextView
    lateinit var txtPageCount: TextView
    lateinit var txtLanguage: TextView
    lateinit var txtAboutBook: TextView
    lateinit var txtRating:TextView
    lateinit var txtPublisherName: TextView
    lateinit var txtPrice: TextView
    lateinit var txtPublisheDate: TextView
    lateinit var toolbar: Toolbar
    lateinit var queue: RequestQueue
    lateinit var jsonObject: JSONObject
    lateinit var book: Book
    lateinit var llBuy: LinearLayout
    lateinit var buyView: View
    lateinit var progressLayout:RelativeLayout

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
        txtAuthorName = findViewById(R.id.txtAuthorname)
        txtSubtitle = findViewById(R.id.txtSubtitle)
        txtTitle = findViewById(R.id.txtTitle)
        txtPageCount = findViewById(R.id.txtPagecount)
        txtPrice = findViewById(R.id.txtPrice)
        setFav=findViewById(R.id.setFav)
        removeFav=findViewById(R.id.removeFav)
        txtRating=findViewById(R.id.txtRating)
        txtLanguage = findViewById(R.id.txtLanguage)
        txtAboutBook = findViewById(R.id.txtAboutbook)
        txtPublisherName = findViewById(R.id.txtPublishername)
        txtPublisheDate = findViewById(R.id.txtPublishedate)
        toolbar = findViewById(R.id.toolbar)
        llBuy = findViewById(R.id.llBuy)
        buyView = findViewById(R.id.buyView)
        progressLayout=findViewById(R.id.progressLayout)
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
                    if (volumeInfo.has("description")) {
                        val htmlDescription = volumeInfo.getString("description")
                        description = Html.fromHtml(htmlDescription).toString()
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
        progressLayout.visibility=View.GONE
        if (!book.thumbnail.equals("NOT AVAILABLE")) {
            Picasso.get().load(book.thumbnail).error(R.drawable.logo).into(imgBookImage)
        }
        txtAuthorName.text = book.author
        txtSubtitle.text = book.subtitle
        txtRating.text=book.rating.toString()
        txtTitle.text = book.title
        txtPageCount.text = book.pageCount.toString()
        txtLanguage.text = book.language
        txtAboutBook.text = book.description
        txtPublisherName.text = book.publisher
        txtPublisheDate.text = book.publishedDate
        txtPrice.text =
            "This book is also avaliable for online purchase. You can buy this book at:\n" + book.price
    }

    fun WebBuy(view: View) {
        openUrl(book.buyLink)
    }

    fun WebInfo(view: View) {
        openUrl(book.infoLink)
    }

    fun WebPreview(view: View) {
        openUrl(book.previewLink)
    }

    fun openUrl(url: String) {
        val uri: Uri = Uri.parse(url)
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun removeFavourites(view: View) {
        removeFav.visibility=View.GONE
        setFav.visibility=View.VISIBLE

    }
    fun setFavourites(view: View) {
        setFav.visibility=View.GONE
        removeFav.visibility=View.VISIBLE
    }

}