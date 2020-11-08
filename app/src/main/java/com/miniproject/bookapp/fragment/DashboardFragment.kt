package com.miniproject.bookapp.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miniproject.bookapp.R
import com.miniproject.bookapp.activity.BookListActivity
import com.miniproject.bookapp.adapter.DashboardRecyclerAdapter
import com.miniproject.bookapp.model.DashboardChildItem
import com.miniproject.bookapp.model.DashboardParentItem

class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: DashboardRecyclerAdapter
    lateinit var edtSearch: EditText
    lateinit var btnSearch: Button
    var itemList = arrayListOf<DashboardParentItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        edtSearch = view.findViewById(R.id.edtSearch)
        btnSearch = view.findViewById(R.id.btnSearch)

        btnSearch.setOnClickListener {
            val searchTerm = edtSearch.getText().toString()
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
        recyclerAdapter = DashboardRecyclerAdapter(activity as Context, ParentItemList())
        recyclerDashboard.adapter = recyclerAdapter
        recyclerDashboard.layoutManager = layoutManager

        return view
    }

    private fun ParentItemList(): ArrayList<DashboardParentItem> {
        val item = DashboardParentItem(
            "CATEGORY 1",
            ChildItemList()
        )
        itemList.add(item)
        val item1 = DashboardParentItem(
            "CATEGORY2",
            ChildItemList()
        )
        itemList.add(item1)
        val item2 = DashboardParentItem(
            "CATEGORY 3",
            ChildItemList()
        )
        itemList.add(item2)
        val item3 = DashboardParentItem(
            "CATEGORY 4",
            ChildItemList()
        )
        itemList.add(item3)
        return itemList
    }

    private fun ChildItemList(): ArrayList<DashboardChildItem> {
        val ChildItemList = arrayListOf<DashboardChildItem>()
        ChildItemList.add(DashboardChildItem("IMG1", "TITLE 1", "AUTHOR 1",""))
        ChildItemList.add(DashboardChildItem("IMG2", "TITLE 2", "AUTHOR 2",""))
        ChildItemList.add(DashboardChildItem("IMG3", "TITLE 3", "AUTHOR 3",""))
        ChildItemList.add(DashboardChildItem("IMG4", "TITLE 4", "AUTHOR 4",""))
        return ChildItemList
    }

}