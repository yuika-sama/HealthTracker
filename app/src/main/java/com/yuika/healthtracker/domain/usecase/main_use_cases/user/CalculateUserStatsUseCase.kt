package com.yuika.healthtracker.domain.usecase.main_use_cases.user

import com.yuika.healthtracker.domain.model.User
import javax.inject.Inject
import kotlin.math.roundToInt

data class UserStats(
    val age: Int,
    val bmi: Double,
    val bmiCategory: String,
    val bmr: Double,
    val tdee: Double,
    val goalKcal: Int
)

class CalculateUserStatsUseCase @Inject constructor()
{
    operator fun invoke(user: User): UserStats
    {
        val age = calculateAgeFromDateOfBirth(user.dateOfBirth) ?: user.age
        val hMeter = user.height / 100.0
        val bmi = if (hMeter > 0.0) user.weight / (hMeter * hMeter) else 0.0

        val bmr = when (user.gender.lowercase())
        {
            "male", "nam" -> 10 * user.weight + 6.25 * user.height - 5 * age + 5
            "female", "nu", "nữ" -> 10 * user.weight + 6.25 * user.height - 5 * age - 161
            else -> 10 * user.weight + 6.25 * user.height - 5 * age
        }

        val tdee = bmr * when (user.activityLevel)
        {
            "sedentary" -> 1.2
            "lightly_active" -> 1.375
            "moderately_active" -> 1.55
            "very_active" -> 1.725
            "extra_active" -> 1.9
            else -> 1.55
        }

        val goalKcal = (tdee + when (user.goal)
        {
            "lose_weight" -> -500
            "gain_weight" -> 500
            else -> 0
        }).roundToInt().coerceAtLeast(0)

        return UserStats(
            age = age,
            bmi = bmi,
            bmiCategory = classifyBmi(bmi),
            bmr = bmr,
            tdee = tdee,
            goalKcal = goalKcal
        )
    }

    private fun classifyBmi(bmi: Double): String = when
    {
        bmi <= 0.0 -> "Unknown"
        bmi < 18.5 -> "Underweight"
        bmi < 25.0 -> "Normal"
        bmi < 30.0 -> "Overweight"
        else -> "Obese"
    }
}
