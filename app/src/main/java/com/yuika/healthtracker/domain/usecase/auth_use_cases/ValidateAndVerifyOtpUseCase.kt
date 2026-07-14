package com.yuika.healthtracker.domain.usecase.auth_use_cases

import javax.inject.Inject

class ValidateAndVerifyOtpUseCase @Inject constructor(
    private val verifyOtpUseCase: VerifyOtpUseCase
)
{
    suspend operator fun invoke(otpCode: String, expectedLength: Int) {
        if (otpCode.length < expectedLength) throw IllegalArgumentException("Please enter the full OTP code")
        verifyOtpUseCase(otpCode)
    }
}