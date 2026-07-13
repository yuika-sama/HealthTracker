package com.yuika.healthtracker.domain.usecase.main_use_cases.food

import com.yuika.healthtracker.data.local.entity.FoodEntryEntity
import com.yuika.healthtracker.domain.repository.FoodEntryRepository
import javax.inject.Inject

class SaveFoodEntryUseCase @Inject constructor(
    private val foodEntryRepository: FoodEntryRepository
) {
    suspend operator fun invoke(entry: FoodEntryEntity): Long {
        return foodEntryRepository.insertFoodEntry(entry)
    }
}
