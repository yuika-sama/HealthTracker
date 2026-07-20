package com.yuika.healthtracker.ui.navigation

import androidx.annotation.StringRes
import com.yuika.healthtracker.R

data class AppBarsState(
    val mainTab: String? = null,
    @param:StringRes val titleRes: Int? = null,
    val showBackButton: Boolean = false
)

fun appBarsStateFor(route: String?): AppBarsState {
    if (route == null) return AppBarsState()

    return when {
        route.contains("AddMeal") -> AppBarsState(titleRes = R.string.title_add_meal, showBackButton = true)
        route.contains("AddActivity") -> AppBarsState(titleRes = R.string.title_add_activity, showBackButton = true)
        route.contains("ProfileUpdate") -> AppBarsState(titleRes = R.string.title_update_profile, showBackButton = true)
        route.contains("Onboarding2") -> AppBarsState(titleRes = R.string.title_health_tracker, showBackButton = true)
        route.contains("Onboarding3") -> AppBarsState(titleRes = R.string.title_your_health_goals, showBackButton = true)
        route.contains("Onboarding4") -> AppBarsState(titleRes = R.string.title_goal_calculated)
        route.contains("Dashboard") -> AppBarsState(mainTab = "home")
        route.contains("Diary") -> AppBarsState(mainTab = "diary")
        route.contains("Activity") -> AppBarsState(mainTab = "activity")
        route.contains("Trends") -> AppBarsState(mainTab = "trends")
        route.contains("Profile") -> AppBarsState(mainTab = "profile")
        else -> AppBarsState()
    }
}
