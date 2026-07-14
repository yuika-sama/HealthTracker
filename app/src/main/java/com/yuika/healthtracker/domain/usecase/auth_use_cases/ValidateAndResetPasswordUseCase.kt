package com.yuika.healthtracker.domain.usecase.auth_use_cases

import com.yuika.healthtracker.utils.PASSWORD_REGEX
import javax.inject.Inject

class ValidateAndResetPasswordUseCase @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
)
{
    suspend operator fun invoke(email: String, newPassword: String, confirmPassword: String) {
        if (newPassword.isBlank() || newPassword.isEmpty()) throw IllegalArgumentException("NewPassword_Password cannot be empty")
        if (newPassword.length < 8) throw IllegalArgumentException("NewPassword_Password must be at least 8 characters")
        if (!PASSWORD_REGEX.matches(newPassword)) throw IllegalArgumentException("NewPassword_Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")

        if (confirmPassword.isBlank() || confirmPassword.isEmpty()) throw IllegalArgumentException("ConfirmPassword_Password cannot be empty")
        if (confirmPassword != newPassword) throw IllegalArgumentException("ConfirmPassword_Password do not match")

        resetPasswordUseCase(email, newPassword)
    }
}