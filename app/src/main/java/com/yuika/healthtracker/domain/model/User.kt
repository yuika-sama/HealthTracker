package com.yuika.healthtracker.domain.model

data class User(
    val id: Int = 0,
    val email: String,
    val password: String,
    val name: String,
    val dateOfBirth: String,
    val gender: String,
    val height: Double,
    val weight: Double,
    val activityLevel: String,
    val goal: String,
    val avatarPath: String?,
    val createdAt: Long = System.currentTimeMillis()
)
