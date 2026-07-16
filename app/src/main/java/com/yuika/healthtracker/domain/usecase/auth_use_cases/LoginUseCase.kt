package com.yuika.healthtracker.domain.usecase.auth_use_cases

import com.yuika.healthtracker.domain.repository.UserRepository
import com.yuika.healthtracker.domain.usecase.main_use_cases.catalog.EnsureUserCatalogSeedUseCase
import com.yuika.healthtracker.utils.MOCK_ERROR_LOGIN_EMAIL
import com.yuika.healthtracker.utils.NETWORK_DELAY
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val ensureUserCatalogSeedUseCase: EnsureUserCatalogSeedUseCase
)
{
    suspend operator fun invoke(email: String, password: String) {
        delay(NETWORK_DELAY.toLong().milliseconds)

        if (email == MOCK_ERROR_LOGIN_EMAIL){
            throw Exception("Error connect to server")
        }

        val user = userRepository.getUserByEmail(email)
            ?: throw Exception("Email or password is wrong")

        if (user.password != password) {
            throw Exception("Email or password is wrong")
        }

        ensureUserCatalogSeedUseCase(user.id)
    }
}
