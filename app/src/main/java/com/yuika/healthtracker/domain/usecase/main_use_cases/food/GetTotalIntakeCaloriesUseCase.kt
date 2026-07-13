package com.yuika.healthtracker.domain.usecase.main_use_cases.food

import com.yuika.healthtracker.domain.repository.FoodEntryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTotalIntakeCaloriesUseCase @Inject constructor(
    private val foodEntryRepository: FoodEntryRepository
) {
    operator fun invoke(userId: Int, date: String): Flow<Int?> {
        return foodEntryRepository.getTotalCaloriesByDate(userId, date)
    }
}
