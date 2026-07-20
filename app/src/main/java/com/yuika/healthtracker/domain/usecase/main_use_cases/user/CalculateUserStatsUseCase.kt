package com.yuika.healthtracker.domain.usecase.main_use_cases.user

import android.content.Context
import com.yuika.healthtracker.R
import com.yuika.healthtracker.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.math.roundToInt

data class UserStats(
    val bmi: Double,
    val bmiCategory: String,
    val bmr: Double,
    val tdee: Double,
    val goalKcal: Int
)

class CalculateUserStatsUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(user: User): UserStats {
        val yearsOld = calculateYearsOldFromDateOfBirth(user.dateOfBirth)
            ?: throw IllegalArgumentException(context.getString(R.string.error_date_of_birth_invalid))
        val heightMeter = user.height / 100.0
        val bmi = if (heightMeter > 0.0) user.weight / (heightMeter * heightMeter) else 0.0
        val gender = user.gender.trim().lowercase()

        val bmr = when {
            gender == "male" || gender == "nam" -> 10 * user.weight + 6.25 * user.height - 5 * yearsOld + 5
            gender == "female" || gender == "nu" || gender == "nữ" -> 10 * user.weight + 6.25 * user.height - 5 * yearsOld - 161
            else -> 10 * user.weight + 6.25 * user.height - 5 * yearsOld
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
