package com.yuika.healthtracker.domain.usecase.main_use_cases.profile

import com.yuika.healthtracker.domain.usecase.main_use_cases.user.CalculateUserStatsUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class ProfileData(
    val name: String,
    val subtitle: String,
    val weight: String,
    val height: String,
    val bmi: String,
    val avatarPath: String?,
    val goalTitle: String,
    val goalDescription: String
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

                val goalTitle = when (user.goal)
                {
                    "lose_weight" -> "Lose weight"
                    "gain_weight" -> "Gain weight"
                    else -> "Maintain weight"
                }

                val activityStr = when (user.activityLevel)
                {
                    "sedentary" -> "Sedentary"
                    "lightly_active" -> "Lightly active"
                    "moderately_active" -> "Moderately active"
                    "very_active" -> "Very active"
                    "extra_active" -> "Extra active"
                    else -> "Active"
                }

                val goalDesc = "$goalTitle. Gain goal: ${
                    String.format("%,d", stats.goalKcal).replace(',', '.')
                } kcal per day."

                ProfileData(
                    name = user.name,
                    subtitle = activityStr,
                    weight = "${user.weight} kg",
                    height = "${user.height} cm",
                    bmi = "${
                        String.format("%.1f", stats.bmi).replace(',', '.')
                    } (${stats.bmiCategory})",
                    avatarPath = user.avatarPath,
                    goalTitle = "Current goal",
                    goalDescription = goalDesc
                )
            }
        }
    }
}
