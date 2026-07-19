package com.yuika.healthtracker.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.yuika.healthtracker.ui.features.main_features.onboarding.page1.OnboardingPage1Screen
import com.yuika.healthtracker.ui.features.main_features.onboarding.page2.OnboardingPage2Screen
import com.yuika.healthtracker.ui.features.main_features.onboarding.page3.OnboardingPage3Screen
import com.yuika.healthtracker.ui.features.main_features.onboarding.page4.OnboardingPage4Screen

fun NavGraphBuilder.onboardingNavGraph(
    appNavigator: AppNavigator,
    contentPadding: PaddingValues
)
{
    composable<Route.Onboarding1> {
        OnboardingPage1Screen(
            contentPadding = contentPadding,
            onNavigateNext = { appNavigator.navigate(Route.Onboarding2) },
            onNavigateBack = { appNavigator.popBackStack() }
        )
    }
    composable<Route.Onboarding2> {
        OnboardingPage2Screen(
            contentPadding = contentPadding,
            onNavigateNext = { appNavigator.navigate(Route.Onboarding3) },
            onNavigateBack = { appNavigator.popBackStack() }
        )
    }
    composable<Route.Onboarding3> {
        OnboardingPage3Screen(
            contentPadding = contentPadding,
            onNavigateNext = { appNavigator.navigate(Route.Onboarding4) },
            onNavigateBack = { appNavigator.popBackStack() }
        )
    }
    composable<Route.Onboarding4> {
        OnboardingPage4Screen(
            contentPadding = contentPadding,
            onNavigateNext = {
                appNavigator.navigate(Route.Dashboard) {
                    popUpTo(Route.Onboarding1) { inclusive = true }
                }
            },
            onNavigateBack = { appNavigator.popBackStack() }
        )
    }
}
