package com.yuika.healthtracker.domain.usecase.auth_use_cases

import android.util.Patterns
import com.yuika.healthtracker.utils.PASSWORD_REGEX
import javax.inject.Inject

class ValidateAndRegisterUseCase @Inject constructor(
    private val registerUseCase: RegisterUseCase
)
{
    suspend operator fun invoke(fullName: String, email: String, age: String, password: String, confirmPassword: String){
        if (fullName.isEmpty() || fullName.isBlank()) throw IllegalArgumentException("FullName_Name would not be blank")

        if (email.isBlank() || email.isEmpty()) throw IllegalArgumentException("Email_Email would not be blank")
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) throw IllegalArgumentException("Email_Email format is invalid")

        val ageInt = age.toIntOrNull()
        if (age.isBlank()) throw IllegalArgumentException("Age_Age would not be blank")
        if (ageInt == null || ageInt <= 5 || ageInt > 120) throw IllegalArgumentException("Age_Please enter a valid age (10-120)")

        if (password.isBlank() || password.isEmpty()) throw IllegalArgumentException("Password_Password would not be blank")
        if (password.length < 8) throw IllegalArgumentException("Password_Password length would be longer than 8")
        if (!PASSWORD_REGEX.matches(password)) throw IllegalArgumentException("Password_Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")

        if (confirmPassword.isBlank()) throw IllegalArgumentException("ConfirmPassword_Confirm password would not be blank")
        if (confirmPassword != password) throw IllegalArgumentException("ConfirmPassword_Confirm password do not match")

        registerUseCase(fullName, email, age, password)
    }
}