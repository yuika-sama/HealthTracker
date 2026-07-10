package com.yuika.healthtracker.ui.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
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
import com.yuika.healthtracker.ui.features.main_features.onboarding.OnboardingPage1Screen
import com.yuika.healthtracker.ui.features.main_features.onboarding.OnboardingPage2Screen
import com.yuika.healthtracker.ui.features.main_features.onboarding.OnboardingPage3Screen
import com.yuika.healthtracker.ui.features.main_features.onboarding.OnboardingPage4Screen
import com.yuika.healthtracker.ui.features.main_features.profile.ProfileScreen
import com.yuika.healthtracker.ui.features.main_features.trends.TrendsScreen
import com.yuika.healthtracker.ui.features.main_features.update_profile.UpdateProfileScreen

// TODO: Viewmodel for Screens

fun NavGraphBuilder.authNavGraph(navController: NavHostController){
    composable<Route.Login>{
        val viewModel: LoginViewModel = hiltViewModel()

        LoginScreen(
            viewModel = viewModel,
            onNavigateToClientPage = {
                // todo: onNavigate to dashboard or onboarding screen
                navController.navigate(Route.Dashboard){
                    popUpTo(Route.Login){inclusive = true}
                }
            },
            onNavigateToRegister = {
                navController.navigate(Route.Register)
            },
            onNavigateToForgotPassword = {
                navController.navigate(Route.ForgotPassword)
            }
        )
    }
    composable<Route.Register>{
        RegisterScreen(
            onNavigateToLogin = {
                navController.popBackStack()
            },
            onNavigateToVerifyOtp = { email ->
                navController.navigate(Route.OtpVerify(email = email, isFromRegister = true))
            }
        )
    }
    composable<Route.ForgotPassword>{
        ForgotPasswordScreen(
            onBackToLoginClick = { navController.popBackStack() },
            onSendCodeClick = { email ->
                navController.navigate(Route.OtpVerify(email = email, isFromRegister = false))
            }
        )
    }
    composable<Route.OtpVerify>{
        OtpVerifyScreen(
            onNavigateToHome = {
                navController.navigate(Route.Dashboard) {
                    popUpTo(Route.Login) { inclusive = true }
                }
            },
            onNavigateToCreateNewPassword = { email ->
                navController.navigate(Route.CreateNewPassword(email = email))
            },
            onBackToLoginClick = { navController.popBackStack() }
        )
    }
    composable<Route.CreateNewPassword>{
        CreateNewPasswordScreen(
            onResetPasswordClick = {
                navController.navigate(Route.PasswordChanged)
            },
            onBackToLoginClick = {
                navController.navigate(Route.Login) {
                    popUpTo(Route.Login) { inclusive = true }
                }
            }
        )
    }
    composable<Route.PasswordChanged>{
        PasswordChangedScreen(
            onBackToLoginClick = {
                navController.navigate(Route.Login) {
                    popUpTo(Route.Login) { inclusive = true }
                }
            }
        )
    }
}

fun NavGraphBuilder.onboardingNavGraph(navController: NavHostController){
    composable<Route.Onboarding1>{
        OnboardingPage1Screen()
    }
    composable<Route.Onboarding2>{
        OnboardingPage2Screen()
    }
    composable<Route.Onboarding3>{
        OnboardingPage3Screen()
    }
    composable<Route.Onboarding4>{
        OnboardingPage4Screen()
    }
}

fun NavGraphBuilder.mainNavGraph(navController: NavHostController){
    val handleTabClick: (String) -> Unit = { tab ->
        val targetRoute = when (tab) {
            "home" -> Route.Dashboard
            "diary" -> Route.Diary
            "activity" -> Route.Activity
            "trends" -> Route.Trends
            "profile" -> Route.Profile
            else -> Route.Dashboard
        }
        navController.navigate(targetRoute) {
            popUpTo(Route.Dashboard) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    composable<Route.Dashboard>{
        DashboardScreen(
            onAddMealClick = { navController.navigate(Route.AddMeal) },
            onAddActivityClick = { navController.navigate(Route.AddActivity) },
            onTabClick = handleTabClick
        )
    }

    composable<Route.Diary>{
        DiaryScreen(
            onAddFoodClick = { mealType ->
                navController.navigate(Route.AddMeal)
            },
            onTabClick = handleTabClick
        )
    }
    composable<Route.AddMeal>{
        AddMealScreen(
            onBackClick = { navController.popBackStack() },
            onSaveClick = { navController.popBackStack() }
        )
    }

    composable<Route.Activity>{
        ActivityScreen(
            onAddActivityClick = { navController.navigate(Route.AddActivity) },
            onTabClick = handleTabClick
        )
    }
    composable<Route.AddActivity>{
        AddActivityScreen(
            onBackClick = { navController.popBackStack() },
            onSaveClick = { navController.popBackStack() }
        )
    }

    composable<Route.Trends>{
        TrendsScreen(
            onTabClick = handleTabClick
        )
    }

    composable<Route.Profile>{
        ProfileScreen(
            onLogoutClick = {
                navController.navigate(Route.Login) {
                    popUpTo(Route.Dashboard) { inclusive = true }
                }
            },
            onEditProfileClick = {
                navController.navigate(Route.ProfileUpdate)
            },
            onTabClick = handleTabClick
        )
    }
    composable<Route.ProfileUpdate>{
        UpdateProfileScreen(
            onBackClick = { navController.popBackStack() },
            onSaveClick = { navController.popBackStack() }
        )
    }
}