package com.yuika.healthtracker.domain.usecase.auth_use_cases

import android.util.Patterns
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.validateDateOfBirth
import com.yuika.healthtracker.utils.PASSWORD_REGEX
import javax.inject.Inject

class ValidateAndRegisterUseCase @Inject constructor(
    private val registerUseCase: RegisterUseCase
)
{
    suspend operator fun invoke(
        fullName: String,
        email: String,
        dateOfBirth: String,
        password: String,
        confirmPassword: String
    )
    {
        val trimmedName = fullName.trim()
        val trimmedEmail = email.trim()

        if (trimmedName.isBlank()) {
            throw IllegalArgumentException("Name would not be blank")
        }
        if (trimmedEmail.isBlank()) {
            throw IllegalArgumentException("Email would not be blank")
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            throw IllegalArgumentException("Email format is invalid")
        }
        if (dateOfBirth.isBlank()) {
            throw IllegalArgumentException("Date of birth would not be blank")
        }
        val validDateOfBirth = validateDateOfBirth(dateOfBirth).first
        if (password.isBlank()) {
            throw IllegalArgumentException("Password would not be blank")
        }
        if (password.length < 8) {
            throw IllegalArgumentException("Password length must be at least 8 characters")
        }
        if (!PASSWORD_REGEX.matches(password)) {
            throw IllegalArgumentException("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
        }
        if (confirmPassword.isBlank()) {
            throw IllegalArgumentException("Confirm password would not be blank")
        }
        if (confirmPassword != password) {
            throw IllegalArgumentException("Confirm password do not match")
        }

        registerUseCase(
            fullName = trimmedName,
            email = trimmedEmail,
            dateOfBirth = validDateOfBirth,
            password = password
        )
    }
}
