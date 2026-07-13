package com.yuika.healthtracker.domain.usecase.main_use_cases.dashboard

import com.yuika.healthtracker.domain.usecase.main_use_cases.activity.GetTotalBurnedCaloriesUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.food.GetTotalIntakeCaloriesUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.CalculateUserStatsUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

data class DashboardData(
    val userName: String,
    val intakeCalories: Int,
    val burnedCalories: Int,
    val netBalance: Int,
    val goalCalories: Int,
    val remainingCalories: Int
)

class GetDashboardDataUseCase @Inject constructor(
    private val getLatestUserUseCase: GetLatestUserUseCase,
    private val getTotalIntakeCaloriesUseCase: GetTotalIntakeCaloriesUseCase,
    private val getTotalBurnedCaloriesUseCase: GetTotalBurnedCaloriesUseCase,
    private val calculateUserStatsUseCase: CalculateUserStatsUseCase
) {
    operator fun invoke(dbDateText: String): Flow<DashboardData?> {
        return getLatestUserUseCase().flatMapLatest { user ->
            if (user == null) {
                flowOf(null)
            } else {
                val stats = calculateUserStatsUseCase(user)
                val intakeFlow = getTotalIntakeCaloriesUseCase(user.id, dbDateText)
                val burnedFlow = getTotalBurnedCaloriesUseCase(user.id, dbDateText)
                
                combine(intakeFlow, burnedFlow) { intake, burned ->
                    val safeIntake = intake ?: 0
                    val safeBurned = burned ?: 0
                    val net = safeIntake - safeBurned
                    val remaining = stats.goalKcal - net
                    
                    DashboardData(
                        userName = user.name,
                        intakeCalories = safeIntake,
                        burnedCalories = safeBurned,
                        netBalance = net,
                        goalCalories = stats.goalKcal,
                        remainingCalories = remaining
                    )
                }
            }
        }
    }
}
