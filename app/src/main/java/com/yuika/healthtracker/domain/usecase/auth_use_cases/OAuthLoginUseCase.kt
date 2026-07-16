package com.yuika.healthtracker.domain.usecase.auth_use_cases

import com.yuika.healthtracker.domain.model.User
import com.yuika.healthtracker.domain.repository.UserRepository
import com.yuika.healthtracker.domain.usecase.main_use_cases.catalog.EnsureUserCatalogSeedUseCase
import com.yuika.healthtracker.utils.NETWORK_DELAY
import kotlinx.coroutines.delay
import javax.inject.Inject

class OAuthLoginUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val ensureUserCatalogSeedUseCase: EnsureUserCatalogSeedUseCase
)
{
    suspend operator fun invoke(provider: String)
    {
        delay(NETWORK_DELAY.toLong())

        val oauthEmail = "oauth.user@healthtracker.com"
        val existingUser = userRepository.getUserByEmail(oauthEmail)

        val oauthUser = if (existingUser == null) {
            val dummyUser = User(
                email = oauthEmail,
                password = "oauth_dummy_password",
                name = "$provider User",
                age = 25,
                gender = "Male",
                height = 170.0,
                weight = 65.0,
                activityLevel = "moderately_active",
                goal = "maintain_weight",
                avatarPath = null
            )
            val userId = userRepository.insertUser(dummyUser).toInt()
            dummyUser.copy(id = userId)
        } else {
            existingUser
        }

        ensureUserCatalogSeedUseCase(oauthUser.id)
    }
}
