package com.yuika.healthtracker.domain.usecase.main_use_cases.food

import com.yuika.healthtracker.data.local.entity.FoodEntryEntity
import com.yuika.healthtracker.domain.repository.FoodEntryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFoodEntriesByDateUseCase @Inject constructor(
    private val foodEntryRepository: FoodEntryRepository
) {
    operator fun invoke(userId: Int, date: String): Flow<List<FoodEntryEntity>> {
        return foodEntryRepository.getFoodEntriesByDate(userId, date)
    }
}
