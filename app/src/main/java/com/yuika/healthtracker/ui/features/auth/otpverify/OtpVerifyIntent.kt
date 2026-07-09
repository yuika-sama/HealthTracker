package com.yuika.healthtracker.ui.features.auth.otpverify

import com.yuika.healthtracker.ui.core.base.UiIntent

sealed class OtpVerifyIntent : UiIntent
{
    data class OtpCodeChanged(val code: String) : OtpVerifyIntent()
    object Submit: OtpVerifyIntent()
    object ResendOtp: OtpVerifyIntent()
}