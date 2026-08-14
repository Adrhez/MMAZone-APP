package com.example.mmazone.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(tableName = "events")
data class Event(
    @PrimaryKey val id: String,
    val name: String,
    val date: ZonedDateTime,
    val location: String,
    val description: String?
)
