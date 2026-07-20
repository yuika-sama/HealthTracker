package com.yuika.healthtracker.domain.usecase.main_use_cases.profile

import com.yuika.healthtracker.domain.usecase.main_use_cases.user.CalculateUserStatsUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class ProfileData(
    val name: String,
    val activityLevel: String,
    val weight: String,
    val height: String,
    val bmi: String,
    val bmiCategory: String,
    val avatarPath: String?,
    val goal: String,
    val goalCalories: Int
)

class GetProfileDataUseCase @Inject constructor(
    private val getLatestUserUseCase: GetLatestUserUseCase,
    private val calculateUserStatsUseCase: CalculateUserStatsUseCase
)
{
    operator fun invoke(): Flow<ProfileData?>
    {
        return getLatestUserUseCase().map { user ->
            if (user == null)
            {
                null
            }
            else
            {
                val stats = calculateUserStatsUseCase(user)

                ProfileData(
                    name = user.name,
                    activityLevel = user.activityLevel,
                    weight = "${user.weight} kg",
                    height = "${user.height} cm",
                    bmi = "${
                        String.format("%.1f", stats.bmi).replace(',', '.')
                    }",
                    bmiCategory = stats.bmiCategory,
                    avatarPath = user.avatarPath,
                    goal = user.goal,
                    goalCalories = stats.goalKcal
                )
            }
        }
    }
}
