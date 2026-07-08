package com.yuika.healthtracker.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Route
{
    @Serializable object Login: Route
    @Serializable object Register: Route
    @Serializable object ForgotPassword: Route
    @Serializable data class OtpVerify(val email: String): Route
    @Serializable object CreateNewPassword: Route
    @Serializable object PasswordChanged: Route

    @Serializable object Onboarding1: Route
    @Serializable object Onboarding2: Route
    @Serializable object Onboarding3: Route
    @Serializable object Onboarding4: Route

    @Serializable object Dashboard: Route

    @Serializable object Diary: Route
    @Serializable object AddMeal: Route

    @Serializable object Activity: Route
    @Serializable object AddActivity: Route

    @Serializable object Trends: Route

    @Serializable object Profile: Route
    @Serializable object ProfileUpdate: Route
}