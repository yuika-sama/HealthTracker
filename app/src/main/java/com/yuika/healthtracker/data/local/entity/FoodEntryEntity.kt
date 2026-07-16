package com.yuika.healthtracker.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "food_entries",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userId", "dateText"]),
        Index(value = ["userId"])
    ]
)
// food-user table
data class FoodEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int = 0,
    val foodCatalogId: Int? = null,
    val dateText: String,
    val mealType: String,
    val foodName: String,
    val quantity: Float,
    val unit: String,
    val caloriesPerServing: Int,
    val calories: Int,
    val imagePath: String?,
    val note: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
