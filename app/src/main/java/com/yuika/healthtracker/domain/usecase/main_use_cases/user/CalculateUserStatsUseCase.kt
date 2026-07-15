package com.yuika.healthtracker.domain.usecase.main_use_cases.user

import com.yuika.healthtracker.domain.model.User
import javax.inject.Inject

data class UserStats(
    val bmi: Double,
    val bmr: Double,
    val tdee: Double,
    val goalKcal: Int
)

class CalculateUserStatsUseCase @Inject constructor() {
    operator fun invoke(user: User): UserStats {
        val hMeter = user.height / 100.0
        val bmi = if (hMeter > 0) user.weight / (hMeter * hMeter) else 0.0

        var bmr = (10 * user.weight) + (6.25 * user.height) - (5 * user.age)
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

        return UserStats(
            bmi = bmi,
            bmr = bmr,
            tdee = tdee,
            goalKcal = goalKcal
        )
    }
}
