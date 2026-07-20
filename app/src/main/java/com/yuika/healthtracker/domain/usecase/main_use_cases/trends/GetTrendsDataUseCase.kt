package com.yuika.healthtracker.domain.usecase.main_use_cases.trends

import com.yuika.healthtracker.domain.usecase.main_use_cases.activity.GetActivitiesByDateRangeUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.food.GetFoodEntriesByDateRangeUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.CalculateUserStatsUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import javax.inject.Inject

data class TrendsChartDataPoint(
    val label: String,
    val value: Float,
    val dateText: String,
    val intake: Int,
    val burned: Int
){
    val balance: Int get() = intake - burned
}

data class TrendsData(
    val avgIntakeStr: String,
    val avgBurnedStr: String,
    val daysMeetingGoal: String,
    val goalDays: String,
    val intakeChartData: List<TrendsChartDataPoint>,
    val weeklyTrendChartData: List<TrendsChartDataPoint>
)

class GetTrendsDataUseCase @Inject constructor(
    private val getLatestUserUseCase: GetLatestUserUseCase,
    private val getFoodEntriesByDateRangeUseCase: GetFoodEntriesByDateRangeUseCase,
    private val getActivitiesByDateRangeUseCase: GetActivitiesByDateRangeUseCase,
    private val calculateUserStatsUseCase: CalculateUserStatsUseCase
)
{
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(startDate: LocalDate, endDate: LocalDate): Flow<TrendsData?>
    {
        val start = minOf(startDate, endDate)
        val end = maxOf(startDate, endDate)

        return getLatestUserUseCase().flatMapLatest { user ->
            if (user == null) return@flatMapLatest flowOf(null)

            combine(
                getFoodEntriesByDateRangeUseCase(user.id, start.toString(), end.toString()),
                getActivitiesByDateRangeUseCase(user.id, start.toString(), end.toString())
            ) { foods, activities ->
                val goalKcal = calculateUserStatsUseCase(user).goalKcal
                val foodsByDate = foods.groupBy { it.dateText }
                val activitiesByDate = activities.groupBy { it.dateText }

                fun totals(date: LocalDate): Pair<Int, Int> {
                    val key = date.toString()
                    val intake = foodsByDate[key]?.sumOf { it.calories } ?: 0
                    val burned = activitiesByDate[key]?.sumOf { it.kcalBurned } ?: 0
                    return Pair(intake, burned)
                }

                val days = generateSequence(start) { it.plusDays(1) }
                    .takeWhile { !it.isAfter(end) }
                    .toList()

                val dailyPoints = days.map { date ->
                    val (intake, burned) = totals(date)
                    TrendsChartDataPoint(
                        label = date.label(),
                        value = intake.toFloat(),
                        dateText = date.toString(),
                        intake = intake,
                        burned = burned
                    )
                }

                val netPoints = days.map { date ->
                    val (intake, burned) = totals(date)
                    TrendsChartDataPoint(
                        label = date.label(),
                        value = (intake - burned).toFloat(),
                        dateText = date.toString(),
                        intake = intake,
                        burned = burned
                    )
                }

                val totalIntake = dailyPoints.sumOf { it.intake }
                val totalBurned = dailyPoints.sumOf { it.burned }
                val totalDays = days.size.coerceAtLeast(1)
                val daysMeetingGoal = dailyPoints.count{it.intake > 0 && it.balance in (goalKcal - 300) .. (goalKcal + 200)}

                TrendsData(
                    avgIntakeStr = "%,d".format(totalIntake / totalDays).replace(',', '.'),
                    avgBurnedStr = "%,d".format(totalBurned / totalDays).replace(',', '.'),
                    daysMeetingGoal = daysMeetingGoal.toString(),
                    goalDays = totalDays.toString(),
                    intakeChartData = dailyPoints,
                    weeklyTrendChartData = netPoints
                )
            }
        }
    }

    private fun LocalDate.label() = "${dayOfMonth}/${monthValue}"
}
