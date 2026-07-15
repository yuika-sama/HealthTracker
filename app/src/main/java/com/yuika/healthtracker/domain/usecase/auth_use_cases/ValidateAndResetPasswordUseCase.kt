package com.yuika.healthtracker.domain.usecase.auth_use_cases

import android.util.Patterns
import com.yuika.healthtracker.utils.PASSWORD_REGEX
import javax.inject.Inject

class ValidateAndResetPasswordUseCase @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
)
{
    suspend operator fun invoke(email: String, newPassword: String, confirmPassword: String)
    {
        val trimmedEmail = email.trim()

        if (trimmedEmail.isBlank()) {
            throw IllegalArgumentException("Email is required")
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            throw IllegalArgumentException("Invalid email format")
        }
        if (newPassword.isBlank()) {
            throw IllegalArgumentException("Password cannot be empty")
        }
        if (newPassword.length < 8) {
            throw IllegalArgumentException("Password must be at least 8 characters")
        }
        if (!PASSWORD_REGEX.matches(newPassword)) {
            throw IllegalArgumentException("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
        }
        if (confirmPassword.isBlank()) {
            throw IllegalArgumentException("Password cannot be empty")
        }
        if (confirmPassword != newPassword) {
            throw IllegalArgumentException("Password do not match")
        }

        resetPasswordUseCase(trimmedEmail, newPassword)
    }
}
