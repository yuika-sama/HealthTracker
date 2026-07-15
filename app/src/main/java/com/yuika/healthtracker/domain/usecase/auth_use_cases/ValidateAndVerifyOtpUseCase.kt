package com.yuika.healthtracker.domain.usecase.auth_use_cases

import javax.inject.Inject

class ValidateAndVerifyOtpUseCase @Inject constructor(
    private val verifyOtpUseCase: VerifyOtpUseCase
)
{
    suspend operator fun invoke(otpCode: String, expectedLength: Int) {
        if (expectedLength <= 0) {
            throw IllegalArgumentException("OTP length is not valid")
        }
        if (otpCode.isBlank()) {
            throw IllegalArgumentException("Please enter the OTP code")
        }
        if (otpCode.length < expectedLength) {
            throw IllegalArgumentException("Please enter the full OTP code")
        }
        if (otpCode.length > expectedLength) {
            throw IllegalArgumentException("OTP code is too long")
        }
        if (!otpCode.all { it.isDigit() }) {
            throw IllegalArgumentException("OTP code must contain digits only")
        }

        verifyOtpUseCase(otpCode)
    }
}
