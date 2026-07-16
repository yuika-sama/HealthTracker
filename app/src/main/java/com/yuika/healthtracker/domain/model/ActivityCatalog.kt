package com.yuika.healthtracker.domain.model

data class ActivityCatalog(
    val id: Int = 0,
    val userId: Int,
    val name: String,
    val met: Double,
    val iconName: String,
    val intensity: String
)
