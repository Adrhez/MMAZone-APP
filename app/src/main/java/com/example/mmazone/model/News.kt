package com.example.mmazone.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(tableName = "news")
data class News(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
    val author: String,
    val publishDate: ZonedDateTime,
    val imageUrl: String?
)
