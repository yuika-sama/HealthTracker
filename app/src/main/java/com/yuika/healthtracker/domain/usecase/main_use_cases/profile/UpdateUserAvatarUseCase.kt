package com.yuika.healthtracker.domain.usecase.main_use_cases.profile

import android.content.Context
import com.yuika.healthtracker.R
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.UpdateUserUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class UpdateUserAvatarUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getLatestUserUseCase: GetLatestUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) {
    suspend operator fun invoke(avatarPath: String) {
        val path = avatarPath.trim()
        if (path.isBlank()) throw IllegalArgumentException(context.getString(R.string.error_select_avatar))

        val user = getLatestUserUseCase().firstOrNull()
            ?: throw IllegalArgumentException(context.getString(R.string.error_cannot_find_user_info))

        updateUserUseCase(user.copy(avatarPath = path))
    }
}
