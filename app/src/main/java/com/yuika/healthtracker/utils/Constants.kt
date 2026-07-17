package com.yuika.healthtracker.utils

import java.time.format.DateTimeFormatter
import java.util.Locale

const val NETWORK_DELAY = 1500
const val UI_LOADING_DELAY_MS = NETWORK_DELAY.toLong()

val DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMM dd", Locale.ENGLISH)
