package com.yuika.healthtracker.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.yuika.healthtracker.ui.features.main_features.activity.ActivityScreen
import com.yuika.healthtracker.ui.features.main_features.add_activity.AddActivityScreen
import com.yuika.healthtracker.ui.features.main_features.add_meal.AddMealScreen
import com.yuika.healthtracker.ui.features.main_features.dashboard.DashboardScreen
import com.yuika.healthtracker.ui.features.main_features.diary.DiaryScreen
import com.yuika.healthtracker.ui.features.main_features.profile.ProfileScreen
import com.yuika.healthtracker.ui.features.main_features.trends.TrendsScreen
import com.yuika.healthtracker.ui.features.main_features.update_profile.UpdateProfileScreen
import com.yuika.healthtracker.utils.getMealIntentForCurrentTime
import java.time.LocalDate

fun NavGraphBuilder.mainNavGraph(
    appNavigator: AppNavigator,
    contentPadding: PaddingValues
)
{
    composable<Route.Dashboard> {
        DashboardScreen(
            contentPadding = contentPadding,
            onAddMealClick = {
                appNavigator.navigate(
                    Route.AddMeal(
                        getMealIntentForCurrentTime(),
                        LocalDate.now().toString()
                    )
                )
            },
            onAddActivityClick = { appNavigator.navigate(Route.AddActivity(LocalDate.now().toString())) }
        )
    }

    composable<Route.Diary> {
        DiaryScreen(
            contentPadding = contentPadding,
            onAddFoodClick = { mealType, dateText ->
                appNavigator.navigate(Route.AddMeal(mealType, dateText))
            }
        )
    }
    composable<Route.AddMeal> {
        val route = it.toRoute<Route.AddMeal>()
        AddMealScreen(
            contentPadding = contentPadding,
            mealType = route.mealType,
            dateText = route.dateText,
            onSaveClick = { appNavigator.popBackStack() }
        )
    }

    composable<Route.Activity> {
        ActivityScreen(
            contentPadding = contentPadding,
            onAddActivityClick = { dateText -> appNavigator.navigate(Route.AddActivity(dateText)) }
        )
    }
    composable<Route.AddActivity> {
        val route = it.toRoute<Route.AddActivity>()
        AddActivityScreen(
            contentPadding = contentPadding,
            dateText = route.dateText,
            onSaveClick = { appNavigator.popBackStack() }
        )
    }

    composable<Route.Trends> {
        TrendsScreen(
            contentPadding = contentPadding
        )
    }

    composable<Route.Profile> {
        ProfileScreen(
            contentPadding = contentPadding,
            onEditProfileClick = {
                appNavigator.navigate(Route.ProfileUpdate)
            }
        )
    }
    composable<Route.ProfileUpdate> {
        UpdateProfileScreen(
            contentPadding = contentPadding,
            onBackClick = { appNavigator.popBackStack() }
        )
    }
}
