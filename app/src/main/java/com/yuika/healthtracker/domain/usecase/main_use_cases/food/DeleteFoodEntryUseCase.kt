package com.yuika.healthtracker.domain.usecase.main_use_cases.food

import com.yuika.healthtracker.domain.repository.FoodEntryRepository
import javax.inject.Inject

class DeleteFoodEntryUseCase @Inject constructor(
    private val foodEntryRepository: FoodEntryRepository
)
{
    suspend operator fun invoke(id: Int) = foodEntryRepository.deleteFoodEntryById(id)
}