package com.example.mmazone.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "fighters")
data class Fighter(
    @PrimaryKey val id: String,
    val name: String,
    val nickname: String?,
    val weightClass: String,
    val wins: Int,
    val losses: Int,
    val draws: Int,
    val birthDate: LocalDate?,
    val nationality: String,
    val history: List<FightResult>,
    val imageUrl: String?
)
