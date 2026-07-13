package com.yuika.healthtracker.domain.usecase.main_use_cases.trends

import com.yuika.healthtracker.domain.usecase.main_use_cases.activity.GetActivitiesByDateRangeUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.food.GetFoodEntriesByDateRangeUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.CalculateUserStatsUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import javax.inject.Inject

data class TrendsChartDataPoint(
    val label: String,
    val value: Float
)

data class TrendsData(
    val avgIntakeStr: String,
    val avgBurnedStr: String,
    val daysMeetingGoal: String,
    val goalDays: String,
    val intakeChartData: List<TrendsChartDataPoint>,
    val netCaloriesChartData: List<TrendsChartDataPoint>
)

class GetTrendsDataUseCase @Inject constructor(
    private val getLatestUserUseCase: GetLatestUserUseCase,
    private val getFoodEntriesByDateRangeUseCase: GetFoodEntriesByDateRangeUseCase,
    private val getActivitiesByDateRangeUseCase: GetActivitiesByDateRangeUseCase,
    private val calculateUserStatsUseCase: CalculateUserStatsUseCase
) {
    operator fun invoke(period: String): Flow<TrendsData?> {
        val endDate = LocalDate.now()
        val daysToSubtract = if (period == "Week") 6L else 29L

        val startDate = endDate.minusDays(daysToSubtract)

        val startStr = startDate.toString()
        val endStr = endDate.toString()
        val totalDays = (daysToSubtract + 1).toInt()

        return getLatestUserUseCase().flatMapLatest { user ->
            if (user == null) {
                flowOf(null)
            } else {
                val foodFlow = getFoodEntriesByDateRangeUseCase(user.id, startStr, endStr)
                val activityFlow = getActivitiesByDateRangeUseCase(user.id, startStr, endStr)

                combine(foodFlow, activityFlow) { foodEntries, activities ->
                    val totalIntake = foodEntries.sumOf { it.calories }
                    val avgIntake = if (totalDays > 0) totalIntake / totalDays else 0

                    val totalBurned = activities.sumOf { it.kcalBurned }
                    val avgBurned = if (totalDays > 0) totalBurned / totalDays else 0

                    val stats = calculateUserStatsUseCase(user)
                    val goalKcal = stats.goalKcal

                    val intakeByDate = foodEntries.groupBy { it.dateText }
                    val burnByDate = activities.groupBy { it.dateText }
                    var daysMeetingGoal = 0

                    val intakeChartData = mutableListOf<TrendsChartDataPoint>()
                    val netCaloriesChartData = mutableListOf<TrendsChartDataPoint>()

                    var currentDay = startDate
                    while (!currentDay.isAfter(endDate)) {
                        val dateKey = currentDay.toString()
                        val dayIntake = intakeByDate[dateKey]?.sumOf { it.calories } ?: 0
                        val dayBurn = burnByDate[dateKey]?.sumOf { it.kcalBurned } ?: 0

                        if (dayIntake > 0 && dayIntake <= goalKcal + 200) {
                            daysMeetingGoal++
                        }

                        val label = if (period == "Week") {
                            currentDay.dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
                        } else {
                            "${currentDay.dayOfMonth}/${currentDay.monthValue}"
                        }

                        intakeChartData.add(TrendsChartDataPoint(label, dayIntake.toFloat()))
                        val netCalories = dayIntake - dayBurn
                        netCaloriesChartData.add(TrendsChartDataPoint(label, netCalories.toFloat()))

                        currentDay = currentDay.plusDays(1)
                    }

                    TrendsData(
                        avgIntakeStr = String.format("%,d", avgIntake).replace(',', '.'),
                        avgBurnedStr = String.format("%,d", avgBurned).replace(',', '.'),
                        daysMeetingGoal = daysMeetingGoal.toString(),
                        goalDays = "/ $totalDays days",
                        intakeChartData = intakeChartData,
                        netCaloriesChartData = netCaloriesChartData
                    )
                }
            }
        }
    }
}
