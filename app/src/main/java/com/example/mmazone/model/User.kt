package com.example.mmazone.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val username: String,
    val email: String,
    val joinDate: ZonedDateTime,
    val profileImageUrl: String?
)
