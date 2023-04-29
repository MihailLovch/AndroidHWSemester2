package com.example.androidhwsemester2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "tokens"
)
data class TokenEntity(
    @PrimaryKey(true)
    val id: Int,
    val token: String
)
