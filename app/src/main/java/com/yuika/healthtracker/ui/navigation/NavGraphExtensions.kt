package com.yuika.healthtracker.ui.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.yuika.healthtracker.ui.features.auth.create_new_password.CreateNewPasswordScreen
import com.yuika.healthtracker.ui.features.auth.forgot_password.ForgotPasswordScreen
import com.yuika.healthtracker.ui.features.auth.login.LoginScreen
import com.yuika.healthtracker.ui.features.auth.login.LoginViewModel
import com.yuika.healthtracker.ui.features.auth.otpverify.OtpVerifyScreen
import com.yuika.healthtracker.ui.features.auth.password_changed.PasswordChangedScreen
import com.yuika.healthtracker.ui.features.auth.register.RegisterScreen
import com.yuika.healthtracker.ui.features.main_features.activity.ActivityScreen
import com.yuika.healthtracker.ui.features.main_features.add_activity.AddActivityScreen
import com.yuika.healthtracker.ui.features.main_features.add_meal.AddMealScreen
import com.yuika.healthtracker.ui.features.main_features.dashboard.DashboardScreen
import com.yuika.healthtracker.ui.features.main_features.diary.DiaryScreen
import com.yuika.healthtracker.ui.features.main_features.onboarding.page1.OnboardingPage1Screen
import com.yuika.healthtracker.ui.features.main_features.onboarding.page2.OnboardingPage2Screen
import com.yuika.healthtracker.ui.features.main_features.onboarding.page3.OnboardingPage3Screen
import com.yuika.healthtracker.ui.features.main_features.onboarding.page4.OnboardingPage4Screen
import com.yuika.healthtracker.ui.features.main_features.profile.ProfileScreen
import com.yuika.healthtracker.ui.features.main_features.trends.TrendsScreen
import com.yuika.healthtracker.ui.features.main_features.update_profile.UpdateProfileScreen
import com.yuika.healthtracker.utils.getMealIntentForCurrentTime
import java.time.LocalDate

fun NavGraphBuilder.authNavGraph(appNavigator: AppNavigator)
{
    composable<Route.Login> {
        val viewModel: LoginViewModel = hiltViewModel()

        LoginScreen(
            viewModel = viewModel,
            onNavigateToClientPage = {
                appNavigator.navigate(Route.Dashboard) {
                    popUpTo(Route.Login) { inclusive = true }
                }
            },
            onNavigateToRegister = {
                appNavigator.navigate(Route.Register)
            },
            onNavigateToForgotPassword = {
                appNavigator.navigate(Route.ForgotPassword)
            }
        )
    }
    composable<Route.Register> {
        RegisterScreen(
            onNavigateToLogin = {
                appNavigator.popBackStack()
            },
            onNavigateToVerifyOtp = { email ->
                appNavigator.navigate(Route.OtpVerify(email = email, isFromRegister = true))
            }
        )
    }
    composable<Route.ForgotPassword> {
        ForgotPasswordScreen(
            onBackToLoginClick = { appNavigator.popBackStack() },
            onSendCodeClick = { email ->
                appNavigator.navigate(Route.OtpVerify(email = email, isFromRegister = false))
            }
        )
    }
    composable<Route.OtpVerify> {
        OtpVerifyScreen(
            onNavigateToHome = {
                appNavigator.navigate(Route.Onboarding1) {
                    popUpTo(Route.Login) { inclusive = true }
                }
            },
            onNavigateToCreateNewPassword = { email ->
                appNavigator.navigate(Route.CreateNewPassword(email = email))
            },
            onBackToLoginClick = { appNavigator.popBackStack() }
        )
    }
    composable<Route.CreateNewPassword> {
        CreateNewPasswordScreen(
            onResetPasswordClick = {
                appNavigator.navigate(Route.PasswordChanged)
            },
            onBackToLoginClick = {
                appNavigator.navigate(Route.Login) {
                    popUpTo(Route.Login) { inclusive = true }
                }
            }
        )
    }
    composable<Route.PasswordChanged> {
        PasswordChangedScreen(
            onBackToLoginClick = {
                appNavigator.navigate(Route.Login) {
                    popUpTo(Route.Login) { inclusive = true }
                }
            }
        )
    }
}

fun NavGraphBuilder.onboardingNavGraph(appNavigator: AppNavigator)
{
    composable<Route.Onboarding1> {
        OnboardingPage1Screen(
            onNavigateNext = { appNavigator.navigate(Route.Onboarding2) },
            onNavigateBack = { appNavigator.popBackStack() }
        )
    }
    composable<Route.Onboarding2> {
        OnboardingPage2Screen(
            onNavigateNext = { appNavigator.navigate(Route.Onboarding3) },
            onNavigateBack = { appNavigator.popBackStack() }
        )
    }
    composable<Route.Onboarding3> {
        OnboardingPage3Screen(
            onNavigateNext = { appNavigator.navigate(Route.Onboarding4) },
            onNavigateBack = { appNavigator.popBackStack() }
        )
    }
    composable<Route.Onboarding4> {
        OnboardingPage4Screen(
            onNavigateNext = {
                appNavigator.navigate(Route.Dashboard) {
                    popUpTo(Route.Onboarding1) { inclusive = true }
                }
            },
            onNavigateBack = { appNavigator.popBackStack() }
        )
    }
}

fun NavGraphBuilder.mainNavGraph(appNavigator: AppNavigator)
{
    val handleTabClick: (String) -> Unit = { tab ->
        val targetRoute = when (tab)
        {
            "home" -> Route.Dashboard
            "diary" -> Route.Diary
            "activity" -> Route.Activity
            "trends" -> Route.Trends
            "profile" -> Route.Profile
            else -> Route.Dashboard
        }
        appNavigator.navigate(targetRoute) {
            popUpTo(Route.Dashboard) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    composable<Route.Dashboard> {
        DashboardScreen(
            onAddMealClick = {
                appNavigator.navigate(
                    Route.AddMeal(
                        getMealIntentForCurrentTime(),
                        LocalDate.now().toString()
                    )
                )
            },
            onAddActivityClick = { appNavigator.navigate(Route.AddActivity) },
            onTabClick = handleTabClick
        )
    }

    composable<Route.Diary> {
        DiaryScreen(
            onAddFoodClick = { mealType, dateText ->
                appNavigator.navigate(Route.AddMeal(mealType, dateText))
            },
            onTabClick = handleTabClick
        )
    }
    composable<Route.AddMeal> {
        val route = it.toRoute<Route.AddMeal>()
        AddMealScreen(
            mealType = route.mealType,
            dateText = route.dateText,
            onBackClick = { appNavigator.popBackStack() },
            onSaveClick = { appNavigator.popBackStack() }
        )
    }

    composable<Route.Activity> {
        ActivityScreen(
            onAddActivityClick = { appNavigator.navigate(Route.AddActivity) },
            onTabClick = handleTabClick
        )
    }
    composable<Route.AddActivity> {
        AddActivityScreen(
            onBackClick = { appNavigator.popBackStack() },
            onSaveClick = { appNavigator.popBackStack() }
        )
    }

    composable<Route.Trends> {
        TrendsScreen(
            onTabClick = handleTabClick
        )
    }

    composable<Route.Profile> {
        ProfileScreen(
            onLogoutClick = {
                appNavigator.navigate(Route.Login) {
                    popUpTo(Route.Dashboard) { inclusive = true }
                }
            },
            onEditProfileClick = {
                appNavigator.navigate(Route.ProfileUpdate)
            },
            onTabClick = handleTabClick
        )
    }
    composable<Route.ProfileUpdate> {
        UpdateProfileScreen(
            onBackClick = { appNavigator.popBackStack() }
        )
    }
}
