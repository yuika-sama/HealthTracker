package com.yuika.healthtracker.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "activities",
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
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int = 0,
    val name: String,
    val iconName: String,
    val kcalPerHour: Int,
    val durationMins: Int,
    val intensity: String,
    val isManual: Boolean,
    val kcalBurned: Int,
    val dateText: String,
    val timestamp: Long = System.currentTimeMillis()
)
