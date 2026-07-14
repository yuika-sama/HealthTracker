package com.yuika.healthtracker.domain.usecase.main_use_cases.activity

import com.yuika.healthtracker.ui.core.model.IntensityLevel
import javax.inject.Inject

class ParseActivityIntensityUseCase @Inject constructor() {
    operator fun invoke(intensityString: String): IntensityLevel {
        return try {
            IntensityLevel.valueOf(intensityString.uppercase())
        } catch (e: Exception) {
            IntensityLevel.entries.firstOrNull { it.displayName.equals(intensityString, ignoreCase = true) }
                ?: IntensityLevel.LIGHT
        }
    }
}
