package com.yuika.healthtracker.domain.model

data class Activity(
    val id: Int = 0,
    val userId: Int = 0,
    val name: String,
    val iconName: String,
    val kcalPerHour: Int,
    val durationMins: Int,
    val intensity: String,
    val kcalBurned: Int,
    val dateText: String,
    val timestamp: Long = System.currentTimeMillis()
)
