package com.yuika.healthtracker.domain.usecase.auth_use_cases

import com.yuika.healthtracker.utils.NETWORK_DELAY
import com.yuika.healthtracker.utils.TRUE_OTP
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class VerifyOtpUseCase @Inject constructor()
{
    suspend operator fun invoke(otpCode: String) {
        delay(NETWORK_DELAY.toLong().milliseconds)
        if (otpCode != TRUE_OTP) {
            throw Exception("Invalid OTP")
        }
    }
}
