package com.yuika.healthtracker.ui.features.auth.otpverify

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.yuika.healthtracker.domain.usecase.auth_use_cases.ResendOtpUseCase
import com.yuika.healthtracker.domain.usecase.auth_use_cases.ValidateAndVerifyOtpUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.ui.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OtpVerifyViewModel @Inject constructor(
    private val validateAndVerifyOtpUseCase: ValidateAndVerifyOtpUseCase,
    private val resendOtpUseCase: ResendOtpUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<OtpVerifyUiState, OtpVerifyIntent, OtpVerifyEffect>(
    initialState = OtpVerifyUiState()
) {
    private val route = savedStateHandle.toRoute<Route.OtpVerify>()

    init {
        updateState { it.copy(email = route.email) }
    }

    override fun onIntent(intent: OtpVerifyIntent) {
        when (intent) {
            is OtpVerifyIntent.OtpCodeChanged -> {
                if (intent.code.length <= state.value.otpLength) {
                    updateState {
                        it.copy(
                            otpCode = intent.code,
                            errorMessage = null,
                            isSuccess = false
                        )
                    }
                }
            }

            is OtpVerifyIntent.Submit -> handleSubmit()
            is OtpVerifyIntent.ResendOtp -> handleResend()
        }
    }

    private fun handleResend() {
        updateState {
            it.copy(
                isLoading = true,
                errorMessage = null,
                isSuccess = false
            )
        }

        launchSafe(
            onError = { throwable ->
                val message = throwable.message ?: "Failed to resend OTP"
                updateState {
                    it.copy(
                        isLoading = false,
                        isSuccess = false,
                        errorMessage = message
                    )
                }
            }
        ) {
            resendOtpUseCase(state.value.email)
            updateState { it.copy(isLoading = false, isSuccess = true) }
            sendEffect(OtpVerifyEffect.ShowToast("OTP has been resend to ${state.value.email}"))
        }
    }

    private fun handleSubmit() {
        val currentState = state.value
        if (currentState.otpCode.length < currentState.otpLength) {
            val message = "Please enter the full OTP code"
            updateState {
                it.copy(
                    isLoading = false,
                    errorMessage = message,
                    isSuccess = false
                )
            }
            return
        }

        updateState { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }

        launchSafe(
            onError = { throwable ->
                val message = throwable.message ?: "Unknown error occurred"
                updateState {
                    it.copy(
                        isLoading = false,
                        isSuccess = false,
                        errorMessage = message
                    )
                }
            }
        ) {
            validateAndVerifyOtpUseCase(currentState.otpCode, currentState.otpLength)

            updateState { it.copy(isLoading = false, isSuccess = true, errorMessage = null) }
            if (route.isFromRegister) {
                sendEffect(OtpVerifyEffect.NavigateToHome)
            } else {
                sendEffect(OtpVerifyEffect.NavigateToCreateNewPassword(route.email))
            }
        }
    }
}
