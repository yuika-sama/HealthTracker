package com.yuika.healthtracker.ui.features.auth.register

import com.yuika.healthtracker.ui.core.base.UiState

data class RegisterUiState(
    val fullName: String = "",
    val fullNameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val age: String = "",
    val ageError: String? = null,
    val passwordLength: Int = 8,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val showPassword: Boolean = false,
    val showConfirmPassword: Boolean = false,
    val agreedToTerms: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) : UiState
