package com.yuika.healthtracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_entries")
// food-user table
data class FoodEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
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
