package com.yuika.healthtracker.domain.model

data class FoodEntry(
    val id: Int = 0,
    val userId: Int = 0,
    val dateText: String,
    val mealType: String,
    val foodName: String,
    val quantity: Float,
    val unit: String,
    val calories: Int,
    val imagePath: String?,
    val timestamp: Long = System.currentTimeMillis()
)
