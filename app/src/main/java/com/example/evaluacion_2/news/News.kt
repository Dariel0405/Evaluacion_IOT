package com.example.evaluacion_2.news

data class News(
    var id: String = "",
    var title: String = "",
    var subtitle: String = "",
    var content: String = "",
    var authorId: String = "",
    var authorName: String = "",
    var status: String = "pending",
    var createdAt: Long = System.currentTimeMillis()
)
