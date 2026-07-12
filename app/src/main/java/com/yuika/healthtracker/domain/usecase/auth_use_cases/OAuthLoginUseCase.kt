package com.yuika.healthtracker.domain.usecase.auth_use_cases

import com.yuika.healthtracker.data.local.entity.UserEntity
import com.yuika.healthtracker.domain.repository.UserRepository
import com.yuika.healthtracker.utils.MOCK_OAUTH_ACCOUNT_ID
import com.yuika.healthtracker.utils.NETWORK_DELAY
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class OAuthLoginUseCase @Inject constructor(
    private val userRepository: UserRepository
)
{
    suspend operator fun invoke(provider: String): UserEntity
    {
        delay(NETWORK_DELAY.toLong().milliseconds)

        val oauthEmail = "oauth.user@healthtracker.com"
        var oauthUser = userRepository.getUserByEmail(oauthEmail)

        if (oauthUser == null) {
            val dummyUser = UserEntity(
                email = oauthEmail,
                password = "oauth_dummy_password",
                name = "$provider User",
                age = 25,
                gender = "Male",
                height = 170.0,
                weight = 65.0,
                activityLevel = "Moderate",
                goal = "Stay Healthy",
                avatarPath = null
            )
            userRepository.insertUser(dummyUser)
            oauthUser = userRepository.getUserByEmail(oauthEmail)
        }
        return oauthUser!!
    }
}