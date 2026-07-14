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
        if (email.isEmpty()) throw IllegalArgumentException("Email_Email would not be blank")
        if (!Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        ) throw IllegalArgumentException("Email_Email format is invalid")

        if (password.isEmpty()) throw IllegalArgumentException("Password_Password would not be blank")
        if (password.length < 8) throw IllegalArgumentException("Password_Password length would be longer than 8")
        if (PASSWORD_REGEX.matches(password)) throw IllegalArgumentException("Password_Password format is invalid")

        loginUseCase(email, password)
    }
}