package com.miniproject.bookapp.model

data class Book(
    val title: String,
    val subtitle: String,
    val author: String,
    val publisher: String,
    val publishedDate: String,
    val description: String,
    val pageCount: Int,
    val rating: Double,
    val thumbnail: String,
    val language: String,
    val previewLink: String,
    val price: String,
    val buyLink: String,
    val infoLink: String
)