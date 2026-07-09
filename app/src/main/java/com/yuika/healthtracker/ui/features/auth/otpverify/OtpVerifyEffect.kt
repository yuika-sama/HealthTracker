package com.yuika.healthtracker.ui.features.auth.otpverify

import com.yuika.healthtracker.ui.core.base.UiEffect

sealed class OtpVerifyEffect : UiEffect
{
    object NavigateToHome : OtpVerifyEffect()
    object NavigateToLogin : OtpVerifyEffect()
    data class ShowToast(val message: String) : OtpVerifyEffect()
}