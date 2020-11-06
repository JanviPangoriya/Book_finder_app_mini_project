package com.miniproject.bookapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miniproject.bookapp.R
import com.miniproject.bookapp.model.DashboardChildItem

class DashboardChildItemRecyclerAdapter(
    val context: Context,
    val itemItem: ArrayList<DashboardChildItem>
) :
    RecyclerView.Adapter<DashboardChildItemRecyclerAdapter.ChildItemViewHolder>() {
    class ChildItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var bookImage: ImageView = view.findViewById(R.id.bookImage)
        var bookName: TextView = view.findViewById(R.id.BookName)
        var authorName: TextView = view.findViewById(R.id.AuthorName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildItemViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.recycler_dashboard_child, parent, false)
        return ChildItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemItem.size
    }

    override fun onBindViewHolder(holder: ChildItemViewHolder, position: Int) {
        val childItem = itemItem[position]
        holder.bookName.text = childItem.title
        holder.authorName.text = childItem.author
    }
}