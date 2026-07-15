package com.yuika.healthtracker.data.repository

import com.yuika.healthtracker.data.local.entity.ActivityEntity
import com.yuika.healthtracker.data.local.entity.DiaryEntity
import com.yuika.healthtracker.data.local.entity.FoodEntryEntity
import com.yuika.healthtracker.data.local.entity.UserEntity
import com.yuika.healthtracker.domain.model.Activity
import com.yuika.healthtracker.domain.model.Diary
import com.yuika.healthtracker.domain.model.FoodEntry
import com.yuika.healthtracker.domain.model.User

fun UserEntity.toDomain() = User(
    id = id,
    email = email,
    password = password,
    name = name,
    age = age,
    gender = gender,
    height = height,
    weight = weight,
    activityLevel = activityLevel,
    goal = goal,
    avatarPath = avatarPath,
    createdAt = createdAt
)

fun User.toEntity() = UserEntity(
    id = id,
    email = email,
    password = password,
    name = name,
    age = age,
    gender = gender,
    height = height,
    weight = weight,
    activityLevel = activityLevel,
    goal = goal,
    avatarPath = avatarPath,
    createdAt = createdAt
)

fun FoodEntryEntity.toDomain() = FoodEntry(
    id = id,
    userId = userId,
    dateText = dateText,
    mealType = mealType,
    foodName = foodName,
    quantity = quantity,
    unit = unit,
    calories = calories,
    imagePath = imagePath,
    timestamp = timestamp
)

fun FoodEntry.toEntity() = FoodEntryEntity(
    id = id,
    userId = userId,
    dateText = dateText,
    mealType = mealType,
    foodName = foodName,
    quantity = quantity,
    unit = unit,
    calories = calories,
    imagePath = imagePath,
    timestamp = timestamp
)

fun ActivityEntity.toDomain() = Activity(
    id = id,
    userId = userId,
    name = name,
    iconName = iconName,
    kcalPerHour = kcalPerHour,
    durationMins = durationMins,
    intensity = intensity,
    kcalBurned = kcalBurned,
    dateText = dateText,
    timestamp = timestamp
)

fun Activity.toEntity() = ActivityEntity(
    id = id,
    userId = userId,
    name = name,
    iconName = iconName,
    kcalPerHour = kcalPerHour,
    durationMins = durationMins,
    intensity = intensity,
    kcalBurned = kcalBurned,
    dateText = dateText,
    timestamp = timestamp
)

fun DiaryEntity.toDomain() = Diary(
    id = id,
    userId = userId,
    dateText = dateText,
    weight = weight,
    stepsCount = stepsCount
)

fun Diary.toEntity() = DiaryEntity(
    id = id,
    userId = userId,
    dateText = dateText,
    weight = weight,
    stepsCount = stepsCount
)
