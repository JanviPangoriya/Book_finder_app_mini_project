package com.miniproject.bookapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miniproject.bookapp.R
import com.miniproject.bookapp.model.DashboardParentItem

class DashboardRecyclerAdapter(val context: Context, val itemItem: ArrayList<DashboardParentItem>) :
    RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

    private var viewPool = RecyclerView.RecycledViewPool()

    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var categoryName: TextView = view.findViewById(R.id.categoryName)
        var childRecycler: RecyclerView = view.findViewById(R.id.childRecycler)
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
        holder.categoryName.text = parentItem.categoryName
        val layoutManager =
            LinearLayoutManager(holder.childRecycler.context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.initialPrefetchItemCount = parentItem.childItemItem.size
        val childItemAdapter = DashboardChildItemRecyclerAdapter(context, parentItem.childItemItem)
        holder.childRecycler.layoutManager = layoutManager
        holder.childRecycler.adapter = childItemAdapter
        holder.childRecycler.setRecycledViewPool(viewPool)
    }

}