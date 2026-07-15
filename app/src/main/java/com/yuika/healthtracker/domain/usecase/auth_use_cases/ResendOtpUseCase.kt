package com.yuika.healthtracker.domain.usecase.auth_use_cases

import com.yuika.healthtracker.utils.NETWORK_DELAY
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class ResendOtpUseCase @Inject constructor() {
    suspend operator fun invoke(email: String) {
        delay(NETWORK_DELAY.toLong().milliseconds)
    }
}
