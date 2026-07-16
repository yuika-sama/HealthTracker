package com.yuika.healthtracker.data.repository

import com.yuika.healthtracker.data.local.entity.ActivityCatalogEntity
import com.yuika.healthtracker.data.local.entity.ActivityEntity
import com.yuika.healthtracker.data.local.entity.DiaryEntity
import com.yuika.healthtracker.data.local.entity.FoodCatalogEntity
import com.yuika.healthtracker.data.local.entity.FoodEntryEntity
import com.yuika.healthtracker.data.local.entity.UserEntity
import com.yuika.healthtracker.domain.model.Activity
import com.yuika.healthtracker.domain.model.ActivityCatalog
import com.yuika.healthtracker.domain.model.Diary
import com.yuika.healthtracker.domain.model.FoodCatalog
import com.yuika.healthtracker.domain.model.FoodEntry
import com.yuika.healthtracker.domain.model.User

fun UserEntity.toDomain() = User(
    id = id,
    email = email,
    password = password,
    name = name,
    dateOfBirth = dateOfBirth,
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
    dateOfBirth = dateOfBirth,
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
    foodCatalogId = foodCatalogId,
    dateText = dateText,
    mealType = mealType,
    foodName = foodName,
    quantity = quantity,
    unit = unit,
    calories = calories,
    caloriesPerServing = caloriesPerServing,
    imagePath = imagePath,
    note = note,
    timestamp = timestamp
)

fun FoodEntry.toEntity() = FoodEntryEntity(
    id = id,
    userId = userId,
    foodCatalogId = foodCatalogId,
    dateText = dateText,
    mealType = mealType,
    foodName = foodName,
    quantity = quantity,
    unit = unit,
    caloriesPerServing = caloriesPerServing,
    calories = calories,
    imagePath = imagePath,
    note = note,
    timestamp = timestamp
)

fun ActivityEntity.toDomain() = Activity(
    id = id,
    userId = userId,
    activityCatalogId = activityCatalogId,
    name = name,
    iconName = iconName,
    kcalPerHour = kcalPerHour,
    met = met,
    weightKg = weightKg,
    durationMins = durationMins,
    intensity = intensity,
    kcalBurned = kcalBurned,
    dateText = dateText,
    timestamp = timestamp
)

fun Activity.toEntity() = ActivityEntity(
    id = id,
    userId = userId,
    activityCatalogId = activityCatalogId,
    name = name,
    iconName = iconName,
    kcalPerHour = kcalPerHour,
    met = met,
    weightKg = weightKg,
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

fun FoodCatalogEntity.toDomain() = FoodCatalog(
    id = id,
    userId = userId,
    name = name,
    caloriesPerServing = caloriesPerServing,
    defaultQuantity = defaultQuantity,
    unit = unit
)

fun FoodCatalog.toEntity() = FoodCatalogEntity(
    id = id,
    userId = userId,
    name = name,
    caloriesPerServing = caloriesPerServing,
    defaultQuantity = defaultQuantity,
    unit = unit
)

fun ActivityCatalogEntity.toDomain() = ActivityCatalog(
    id = id,
    userId = userId,
    name = name,
    met = met,
    iconName = iconName,
    intensity = intensity
)

fun ActivityCatalog.toEntity() = ActivityCatalogEntity(
    id = id,
    userId = userId,
    name = name,
    met = met,
    iconName = iconName,
    intensity = intensity
)
