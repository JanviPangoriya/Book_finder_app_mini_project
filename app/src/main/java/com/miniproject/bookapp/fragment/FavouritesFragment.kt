package com.miniproject.bookapp.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.google.firebase.firestore.FirebaseFirestore
import com.miniproject.bookapp.R
import com.miniproject.bookapp.adapter.BookListAdapter
import com.miniproject.bookapp.model.DashboardChildItem
import org.json.JSONObject

class FavouritesFragment : Fragment() {

    lateinit var rlNoFavContent: RelativeLayout
    var favBookList = arrayListOf<DashboardChildItem>()
    lateinit var fstore: FirebaseFirestore
    lateinit var favrecyclerview: RecyclerView
    lateinit var adapter: BookListAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_favourites, container, false)

        rlNoFavContent = view.findViewById(R.id.rlNoFavContent)
        rlNoFavContent.visibility = View.VISIBLE

        fstore = FirebaseFirestore.getInstance()
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        favrecyclerview = view.findViewById(R.id.favrecyclerview)
        layoutManager = LinearLayoutManager(activity as Context)

        fstore.collection("favbook").get().addOnSuccessListener {result ->

            rlNoFavContent.visibility = View.GONE
            progressLayout.visibility = View.GONE
            progressBar.visibility = View.GONE

            for(document in result){
                var dashboardChildItem = DashboardChildItem(
                    document["thumbnail"].toString(), document["title"].toString(), document["author"].toString(), document["selfLink"].toString()
                    )
                favBookList.add(dashboardChildItem)
            }
        }.addOnFailureListener{
            Toast.makeText(context, "Some error occurred!", Toast.LENGTH_SHORT).show()
        }

        adapter = BookListAdapter(activity as Context, favBookList)
        favrecyclerview.adapter = adapter
        favrecyclerview.layoutManager = layoutManager

        fstore = FirebaseFirestore.getInstance()

        return view
    }

}