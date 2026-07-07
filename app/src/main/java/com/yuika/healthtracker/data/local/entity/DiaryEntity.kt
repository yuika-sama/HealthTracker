package com.yuika.healthtracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary_entries")
data class DiaryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val calories: Int,
    val weight: Float
)