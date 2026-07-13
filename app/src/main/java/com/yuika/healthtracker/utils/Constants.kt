package com.yuika.healthtracker.utils

import java.time.format.DateTimeFormatter
import java.util.Locale

const val NETWORK_DELAY = 1500

val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!\\-_*])(?=\\S+$).{8,}$".toRegex()

val PHONE_REGEX = "^[0-9]{10}$".toRegex()

const val MOCK_ERROR_LOGIN_EMAIL = "error@healthtracker.com"

const val MOCK_OAUTH_ACCOUNT_ID = 0

const val TRUE_OTP = "123456"
const val ERROR_OTP = "111111"
const val INTERNET_OTP = "404404"

val DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMM dd", Locale.ENGLISH)