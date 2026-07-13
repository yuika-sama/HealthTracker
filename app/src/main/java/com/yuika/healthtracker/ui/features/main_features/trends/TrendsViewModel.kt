package com.yuika.healthtracker.ui.features.main_features.trends

import com.yuika.healthtracker.domain.repository.ActivityRepository
import com.yuika.healthtracker.domain.repository.FoodEntryRepository
import com.yuika.healthtracker.domain.repository.UserRepository
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.descriptors.buildSerialDescriptor
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TrendsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val foodEntryRepository: FoodEntryRepository,
    private val activityRepository: ActivityRepository
) : BaseViewModel<TrendsUiState, TrendsIntent, TrendsEffect>(
    initialState = TrendsUiState()
)
{
    override fun onIntent(intent: TrendsIntent)
    {
        when (intent)
        {
            is TrendsIntent.LoadTrendsData ->
            {
                handleFetchTrends(state.value.selectedPeriod)
            }

            is TrendsIntent.OnPeriodChange ->
            {
                updateState { it.copy(selectedPeriod = intent.period) }
                handleFetchTrends(intent.period)
            }
        }
    }

    private fun handleFetchTrends(period: String)
    {
        updateState { it.copy(isLoading = true, errorMessage = null) }

        launchSafe(
            onError = { throwable ->
                updateState { it.copy(isLoading = false, errorMessage = throwable.message) }
                sendEffect(TrendsEffect.ShowError(throwable.message ?: "Can't get data"))
            }
        ) {
            val user = userRepository.getLatestUserFlow().firstOrNull()

            if (user == null)
            {
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Can't find user information"
                    )
                }
                sendEffect(TrendsEffect.ShowError("Can't find user information"))
                return@launchSafe
            }

            val endDate = LocalDate.now()
            val daysToSubtract = if (period == "Week") 6L else 29L

            val startDate = endDate.minusDays(daysToSubtract)

            val startStr = startDate.toString()
            val endStr = endDate.toString()
            val totalDays = (daysToSubtract + 1).toInt()

//            val foodEntriesAsync = async {
//                foodEntryRepository.getFoodEntriesByDateRange(user.id, startStr, endStr)
//                    .firstOrNull() ?: emptyList()
//            }
//            val activitiesAsync = async {
//                activityRepository.getActivitiesByDateRange(user.id, startStr, endStr).firstOrNull()
//                    ?: emptyList()
//            }
//
//            val foodEntries = foodEntriesAsync.await()
//            val activities = activitiesAsync.await()

            val (foodEntries, activities) = coroutineScope {
                val foodEntriesAsync = async {
                    foodEntryRepository.getFoodEntriesByDateRange(user.id, startStr, endStr)
                        .firstOrNull() ?: emptyList()
                }
                val activitiesAsync = async {
                    activityRepository.getActivitiesByDateRange(user.id, startStr, endStr)
                        .firstOrNull() ?: emptyList()
                }

                Pair(foodEntriesAsync.await(), activitiesAsync.await())
            }

            val totalIntake = foodEntries.sumOf { it.calories }
            val avgIntake = if (totalDays > 0) totalIntake/totalDays else 0

            val totalBurned = activities.sumOf { it.kcalBurned }
            val avgBurned = if (totalDays > 0) totalBurned / totalDays else 0

            val weight = user.weight
            val height = user.height
            val age = user.age
            var bmr = (10 * weight) + (6.25 * height) - (5 * age)
            bmr += if (user.gender == "Male") 5.0 else -161.0

            val activityMultiplier = when (user.activityLevel) {
                "sedentary" -> 1.2
                "lightly_active" -> 1.375
                "moderately_active" -> 1.55
                "very_active" -> 1.725
                "extra_active" -> 1.9
                else -> 1.55
            }
            var tdee = bmr * activityMultiplier
            when (user.goal) {
                "lose_weight" -> tdee -= 500
                "gain_weight" -> tdee += 500
            }
            val goalKcal = tdee.toInt()

            val intakeByDate = foodEntries.groupBy { it.dateText }
            val burnByDate = activities.groupBy { it.dateText }
            var daysMeetingGoal = 0

            val intakeChartData = mutableListOf<ChartDataPoint>()
            val netCaloriesChartData = mutableListOf<ChartDataPoint>()

            var currentDay = startDate
            while (!currentDay.isAfter(endDate)){
                val dateKey = currentDay.toString()
                val dayIntake = intakeByDate[dateKey]?.sumOf { it.calories } ?: 0
                val dayBurn = burnByDate[dateKey]?.sumOf { it.kcalBurned } ?: 0

                if (dayIntake > 0 && dayIntake <= goalKcal + 200){
                    daysMeetingGoal ++
                }

                val label = if (period == "Week") {
                    currentDay.dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
                } else {
                    "${currentDay.dayOfMonth}/${currentDay.monthValue}"
                }

                intakeChartData.add(ChartDataPoint(label, dayIntake.toFloat()))
                val netCalories = dayIntake - dayBurn
                netCaloriesChartData.add(ChartDataPoint(label, netCalories.toFloat()))

                currentDay = currentDay.plusDays(1)
            }

            updateState {
                it.copy(
                    avgIntake = String.format("%,d", avgIntake).replace(',', '.'),
                    avgBurned = String.format("%,d", avgBurned).replace(',', '.'),
                    daysMeetingGoal = daysMeetingGoal.toString(),
                    goalDays = "/ $totalDays days",
                    intakeChartData = intakeChartData,
                    netCaloriesChartData = netCaloriesChartData,
                    isLoading = false
                )
            }
        }

    }
}