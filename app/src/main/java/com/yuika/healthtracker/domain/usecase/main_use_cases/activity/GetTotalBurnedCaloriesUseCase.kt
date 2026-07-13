package com.yuika.healthtracker.domain.usecase.main_use_cases.activity

import com.yuika.healthtracker.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTotalBurnedCaloriesUseCase @Inject constructor(
    private val activityRepository: ActivityRepository
) {
    operator fun invoke(userId: Int, date: String): Flow<Int?> {
        return activityRepository.getTotalBurnedCaloriesByDate(userId, date)
    }
}
