package com.yuika.healthtracker.domain.model

data class FoodCatalog(
    val id: Int = 0,
    val userId: Int,
    val name: String,
    val caloriesPerServing: Int,
    val defaultQuantity: Float,
    val unit: String
)
