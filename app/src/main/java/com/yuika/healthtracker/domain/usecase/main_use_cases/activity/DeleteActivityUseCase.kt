package com.yuika.healthtracker.domain.usecase.main_use_cases.activity

import com.yuika.healthtracker.domain.repository.ActivityRepository
import javax.inject.Inject

class DeleteActivityUseCase @Inject constructor(
    private val activityRepository: ActivityRepository
) {
    suspend operator fun invoke(id: Int) = activityRepository.deleteActivityById(id)
}
