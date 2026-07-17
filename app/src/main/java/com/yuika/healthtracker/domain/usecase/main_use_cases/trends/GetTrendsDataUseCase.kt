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
    operator fun invoke(): Flow<TrendsData?>
    {
        val endDate = LocalDate.now()
        val dailyStart = endDate.minusDays(6)
        val trendStart = endDate.minusDays(27)

        return getLatestUserUseCase().flatMapLatest { user ->
            if (user == null) return@flatMapLatest flowOf(null)

            combine(
                getFoodEntriesByDateRangeUseCase(user.id, trendStart.toString(), endDate.toString()),
                getActivitiesByDateRangeUseCase(user.id, trendStart.toString(), endDate.toString())
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

                val days = generateSequence(dailyStart) { it.plusDays(1) }
                    .take(7)
                    .toList()

                val dailyPoints = days.map { date ->
                    val (intake, burned) = totals(date)
                    TrendsChartDataPoint(
                        label = date.dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercase() },
                        value = intake.toFloat(),
                        dateText = date.toString(),
                        intake = intake,
                        burned = burned
                    )
                }

                val weeklyPoints = (0..3).map { index ->
                    val start = trendStart.plusDays(index * 7L)
                    val end = minOf(start.plusDays(6), endDate)
                    val weekDays = generateSequence(start) { it.plusDays(1) }
                        .takeWhile { !it.isAfter(end) }
                        .toList()

                    val intake = weekDays.sumOf { totals(it).first }
                    val burned = weekDays.sumOf { totals(it).second }

                    TrendsChartDataPoint(
                        label =  "${start.dayOfMonth}/${start.monthValue}",
                        value = (intake - burned).toFloat(),
                        dateText = "${start} - ${end}",
                        intake = intake,
                        burned = burned
                    )
                }

                val totalIntake = dailyPoints.sumOf { it.intake }
                val totalBurned = dailyPoints.sumOf { it.burned }
                val daysMeetingGoal = dailyPoints.count{it.intake > 0 && it.balance in (goalKcal - 300) .. (goalKcal + 200)}

                TrendsData(
                    avgIntakeStr = "%,d".format(totalIntake / 7).replace(',', '.'),
                    avgBurnedStr = "%,d".format(totalBurned / 7).replace(',', '.'),
                    daysMeetingGoal = daysMeetingGoal.toString(),
                    goalDays = "/ 7 days",
                    intakeChartData = dailyPoints,
                    weeklyTrendChartData = weeklyPoints
                )
            }
        }
    }
}
