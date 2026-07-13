package com.yuika.healthtracker.domain.usecase.main_use_cases.activity

import com.yuika.healthtracker.data.local.entity.ActivityEntity
import com.yuika.healthtracker.domain.repository.ActivityRepository
import javax.inject.Inject

class SaveActivityUseCase @Inject constructor(
    private val activityRepository: ActivityRepository
) {
    suspend operator fun invoke(activity: ActivityEntity): Long {
        return activityRepository.insertActivity(activity)
    }
}
