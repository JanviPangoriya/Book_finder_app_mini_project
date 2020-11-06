package com.miniproject.bookapp.model

data class DashboardParentItem(
    var categoryName: String,
    var childItemItem: ArrayList<DashboardChildItem>
)