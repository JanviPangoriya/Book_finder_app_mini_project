package com.miniproject.bookapp.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.miniproject.bookapp.R
import com.miniproject.bookapp.adapter.BookListAdapter
import com.miniproject.bookapp.model.DashboardChildItem

class FavouritesFragment : Fragment() {

    lateinit var rlNoFavContent: RelativeLayout
    var favBookList = arrayListOf<DashboardChildItem>()
    lateinit var fstore: FirebaseFirestore
    lateinit var favrecyclerview: RecyclerView
    lateinit var adapter: BookListAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressLayout: RelativeLayout
    lateinit var fauth: FirebaseAuth
    lateinit var uid: String
    var loaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_favourites, container, false)

        rlNoFavContent = view.findViewById(R.id.rlNoFavContent)
        rlNoFavContent.visibility = View.VISIBLE
        fauth = FirebaseAuth.getInstance()
        uid = fauth.currentUser?.uid.toString()
        fstore = FirebaseFirestore.getInstance()
        progressLayout = view.findViewById(R.id.progressLayout)
        favrecyclerview = view.findViewById(R.id.favrecyclerview)
        layoutManager = LinearLayoutManager(activity as Context)

        getList()

        return view
    }

    override fun onResume() {
        super.onResume()
        if (!loaded) {
            loaded = true
        } else {
            favBookList.clear()
            favBookList = getList()
            if (favBookList.size == 0) {
                rlNoFavContent.visibility = View.VISIBLE
            }
            adapter.notifyDataSetChanged()
        }
    }

    fun getList(): ArrayList<DashboardChildItem> {
        fstore.collection("users").document(uid).collection("favbook").get()
            .addOnSuccessListener { result ->

                progressLayout.visibility = View.GONE

                for (document in result) {

                    val data = document.data

                    var dashboardChildItem = DashboardChildItem(
                        data["thumbnail"].toString(),
                        data["title"].toString(),
                        data["author"].toString(),
                        data["selfLink"].toString()
                    )
                    favBookList.add(dashboardChildItem)
                    rlNoFavContent.visibility = View.GONE
                    adapter = BookListAdapter(activity as Context, favBookList)
                    favrecyclerview.adapter = adapter
                    favrecyclerview.layoutManager = layoutManager
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Some error occurred!", Toast.LENGTH_SHORT).show()
            }

        return favBookList
    }

}