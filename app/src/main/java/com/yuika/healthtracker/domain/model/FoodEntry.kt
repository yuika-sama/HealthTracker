package com.yuika.healthtracker.domain.model

data class FoodEntry(
    val id: Int = 0,
    val userId: Int = 0,
    val foodCatalogId: Int? = null,
    val dateText: String,
    val mealType: String,
    val foodName: String,
    val quantity: Float,
    val unit: String,
    val calories: Int,
    val caloriesPerServing: Int,
    val imagePath: String?,
    val note: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
