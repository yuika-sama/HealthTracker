package com.yuika.healthtracker.domain.usecase.main_use_cases.food

import com.yuika.healthtracker.domain.model.FoodEntry
import com.yuika.healthtracker.domain.repository.FoodEntryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFoodEntriesByDateRangeUseCase @Inject constructor(
    private val foodEntryRepository: FoodEntryRepository
) {
    operator fun invoke(userId: Int, startDate: String, endDate: String): Flow<List<FoodEntry>> {
        return foodEntryRepository.getFoodEntriesByDateRange(userId, startDate, endDate)
    }
}
