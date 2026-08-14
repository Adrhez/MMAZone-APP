package com.example.mmazone.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fightResults")
data class FightResult(
    @PrimaryKey val id: String,
    val winnerName: String,
    val loserName: String,
    val method: String,
    val round: Int,
    val time: String,
    val eventName: String,
    val date: Long
)
