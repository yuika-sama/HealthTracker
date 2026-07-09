package com.yuika.healthtracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName =  "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val password: String,
    val name: String,
    val dob: String,
    val gender: String,
    val height: Double,
    val weight: Double,
    val activityLevel: String,
    val goal: String,
    val avatarPath: String?,
    val createdAt: Long = System.currentTimeMillis()
)