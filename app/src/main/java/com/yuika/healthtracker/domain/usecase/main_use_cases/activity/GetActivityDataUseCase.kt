package com.yuika.healthtracker.domain.usecase.main_use_cases.activity

import com.yuika.healthtracker.data.local.entity.ActivityEntity
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.CalculateUserStatsUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class ActivityData(
    val activities: List<ActivityEntity>,
    val burnedKcal: Int,
    val goalKcal: Int
)

class GetActivityDataUseCase @Inject constructor(
    private val getLatestUserUseCase: GetLatestUserUseCase,
    private val getActivitiesByDateUseCase: GetActivitiesByDateUseCase,
    private val calculateUserStatsUseCase: CalculateUserStatsUseCase
) {
    operator fun invoke(dateText: String): Flow<ActivityData?> {
        return getLatestUserUseCase().flatMapLatest { user ->
            if (user == null) {
                flowOf(null)
            } else {
                getActivitiesByDateUseCase(user.id, dateText).map { activities ->
                    val totalBurned = activities.sumOf { it.kcalBurned }
                    val stats = calculateUserStatsUseCase(user)
                    val goalKCal = stats.goalKcal

                    ActivityData(
                        activities = activities,
                        burnedKcal = totalBurned,
                        goalKcal = goalKCal
                    )
                }
            }
        }
    }
}
