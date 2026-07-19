package com.yuika.healthtracker.ui.navigation

data class AppBarsState(
    val mainTab: String? = null,
    val title: String? = null,
    val showBackButton: Boolean = false
)

fun appBarsStateFor(route: String?): AppBarsState {
    if (route == null) return AppBarsState()

    return when {
        route.contains("AddMeal") -> AppBarsState(title = "Add a meal", showBackButton = true)
        route.contains("AddActivity") -> AppBarsState(title = "Add an activity", showBackButton = true)
        route.contains("ProfileUpdate") -> AppBarsState(title = "Update profile", showBackButton = true)
        route.contains("Onboarding2") -> AppBarsState(title = "Health Tracker", showBackButton = true)
        route.contains("Onboarding3") -> AppBarsState(title = "Your Health Goals", showBackButton = true)
        route.contains("Onboarding4") -> AppBarsState(title = "Goal Calculated")
        route.contains("Dashboard") -> AppBarsState(mainTab = "home")
        route.contains("Diary") -> AppBarsState(mainTab = "diary")
        route.contains("Activity") -> AppBarsState(mainTab = "activity")
        route.contains("Trends") -> AppBarsState(mainTab = "trends")
        route.contains("Profile") -> AppBarsState(mainTab = "profile")
        else -> AppBarsState()
    }
}
