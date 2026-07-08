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
                navController.navigate(Route.OtpVerify(email = email))
            }
        )
    }
    composable<Route.ForgotPassword>{
        ForgotPasswordScreen()
    }
    composable<Route.OtpVerify>{
        OtpVerifyScreen()
    }
    composable<Route.CreateNewPassword>{
        CreateNewPasswordScreen()
    }
    composable<Route.PasswordChanged>{
        PasswordChangedScreen()
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
    composable<Route.Dashboard>{
        DashboardScreen()
    }

    composable<Route.Diary>{
        DiaryScreen()
    }
    composable<Route.AddMeal>{
        AddMealScreen()
    }

    composable<Route.Activity>{
        ActivityScreen()
    }
    composable<Route.AddActivity>{
        AddActivityScreen()
    }

    composable<Route.Trends>{
        TrendsScreen()
    }

    composable<Route.Profile>{
        ProfileScreen()
    }
    composable<Route.ProfileUpdate>{
        UpdateProfileScreen()
    }
}