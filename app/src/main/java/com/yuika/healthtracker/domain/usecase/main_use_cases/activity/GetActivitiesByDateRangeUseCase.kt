package com.yuika.healthtracker.domain.usecase.main_use_cases.activity

import com.yuika.healthtracker.domain.model.Activity
import com.yuika.healthtracker.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetActivitiesByDateRangeUseCase @Inject constructor(
    private val activityRepository: ActivityRepository
) {
    operator fun invoke(userId: Int, startDate: String, endDate: String): Flow<List<Activity>> {
        return activityRepository.getActivitiesByDateRange(userId, startDate, endDate)
    }
}
