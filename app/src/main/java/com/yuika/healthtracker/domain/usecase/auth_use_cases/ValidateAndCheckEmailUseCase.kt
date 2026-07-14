package com.yuika.healthtracker.domain.usecase.auth_use_cases

import android.util.Patterns
import javax.inject.Inject

class ValidateAndCheckEmailUseCase @Inject constructor(
    private val checkEmailExistsUseCase: CheckEmailExistsUseCase
)
{
    suspend operator fun invoke(email: String): Boolean {
        if (email.isEmpty() || email.isBlank()) throw IllegalArgumentException("Email_Email is required")
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) throw IllegalArgumentException("Email_Invalid email format")

        return checkEmailExistsUseCase(email)
    }
}