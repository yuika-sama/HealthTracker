package com.yuika.healthtracker.domain.usecase.main_use_cases.activity

import com.yuika.healthtracker.data.local.entity.ActivityEntity
import com.yuika.healthtracker.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetActivitiesByDateUseCase @Inject constructor(
    private val activityRepository: ActivityRepository
) {
    operator fun invoke(userId: Int, date: String): Flow<List<ActivityEntity>> {
        return activityRepository.getActivitiesByDate(userId, date)
    }
}
