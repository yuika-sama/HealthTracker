package com.yuika.healthtracker.domain.usecase.main_use_cases.food

import com.yuika.healthtracker.domain.model.FoodEntry
import com.yuika.healthtracker.domain.repository.FoodEntryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFoodEntriesByDateUseCase @Inject constructor(
    private val foodEntryRepository: FoodEntryRepository
) {
    operator fun invoke(userId: Int, date: String): Flow<List<FoodEntry>> {
        return foodEntryRepository.getFoodEntriesByDate(userId, date)
    }
}
