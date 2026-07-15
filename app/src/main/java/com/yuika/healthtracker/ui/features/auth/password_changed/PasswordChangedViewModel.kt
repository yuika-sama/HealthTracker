package com.yuika.healthtracker.ui.features.auth.password_changed

import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PasswordChangedViewModel @Inject constructor(
) : BaseViewModel<PasswordChangedUiState, PasswordChangedIntent, PasswordChangedEffect>(
    initialState = PasswordChangedUiState()
)
{
    override fun onIntent(intent: PasswordChangedIntent)
    {
        when (intent)
        {
            is PasswordChangedIntent.BackToLoginClick -> sendEffect(PasswordChangedEffect.NavigateToLogin)
        }
    }
}
