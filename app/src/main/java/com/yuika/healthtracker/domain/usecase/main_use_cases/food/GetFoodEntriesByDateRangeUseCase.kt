package com.yuika.healthtracker.domain.usecase.main_use_cases.food

import com.yuika.healthtracker.data.local.entity.FoodEntryEntity
import com.yuika.healthtracker.domain.repository.FoodEntryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFoodEntriesByDateRangeUseCase @Inject constructor(
    private val foodEntryRepository: FoodEntryRepository
) {
    operator fun invoke(userId: Int, startDate: String, endDate: String): Flow<List<FoodEntryEntity>> {
        return foodEntryRepository.getFoodEntriesByDateRange(userId, startDate, endDate)
    }
}
