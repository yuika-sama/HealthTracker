package com.yuika.healthtracker.domain.usecase.auth_use_cases

import android.util.Patterns
import javax.inject.Inject

class ValidateAndCheckEmailUseCase @Inject constructor(
    private val checkEmailExistsUseCase: CheckEmailExistsUseCase
)
{
    suspend operator fun invoke(email: String): Boolean
    {
        val trimmedEmail = email.trim()

        if (trimmedEmail.isBlank()) {
            throw IllegalArgumentException("Email is required")
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            throw IllegalArgumentException("Invalid email format")
        }

        return checkEmailExistsUseCase(trimmedEmail)
    }
}
