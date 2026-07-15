package com.yuika.healthtracker.domain.usecase.main_use_cases.activity

import com.yuika.healthtracker.domain.model.Activity
import com.yuika.healthtracker.domain.repository.ActivityRepository
import javax.inject.Inject

class SaveActivityUseCase @Inject constructor(
    private val activityRepository: ActivityRepository
) {
    suspend operator fun invoke(activity: Activity): Long {
        return activityRepository.insertActivity(activity)
    }
}
