package com.yuika.healthtracker.domain.model

data class Diary(
    val id: Int = 0,
    val userId: Int = 0,
    val dateText: String,
    val weight: Float,
    val stepsCount: Int = 0
)
