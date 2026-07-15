package com.yuika.healthtracker.domain.usecase.auth_use_cases

import android.util.Patterns
import com.yuika.healthtracker.utils.PASSWORD_REGEX
import javax.inject.Inject

class ValidateAndLoginUseCase @Inject constructor(
    private val loginUseCase: LoginUseCase
)
{
    suspend operator fun invoke(email: String, password: String)
    {
        val trimmedEmail = email.trim()

        if (trimmedEmail.isBlank()) {
            throw IllegalArgumentException("Email would not be blank")
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            throw IllegalArgumentException("Email format is invalid")
        }
        if (password.isBlank()) {
            throw IllegalArgumentException("Password would not be blank")
        }
        if (password.length < 8) {
            throw IllegalArgumentException("Password length must be at least 8 characters")
        }
        if (!PASSWORD_REGEX.matches(password)) {
            throw IllegalArgumentException("Password format is invalid")
        }

        loginUseCase(trimmedEmail, password)
    }
}
