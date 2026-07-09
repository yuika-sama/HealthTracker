package com.yuika.healthtracker.domain.usecase.auth_use_cases

import com.yuika.healthtracker.domain.repository.UserRepository
import com.yuika.healthtracker.utils.NETWORK_DELAY
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class ResetPasswordUseCase @Inject constructor(
    private val userRepository: UserRepository
)
{
    suspend operator fun invoke(email: String?, newPasswordString: String){
        // todo: update user
        delay(NETWORK_DELAY.toLong().milliseconds)
    }
}