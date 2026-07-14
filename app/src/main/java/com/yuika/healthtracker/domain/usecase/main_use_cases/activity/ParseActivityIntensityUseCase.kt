package com.yuika.healthtracker.domain.usecase.main_use_cases.activity

import com.yuika.healthtracker.ui.features.main_features.activity.components.IntensityLevel
import javax.inject.Inject

class ParseActivityIntensityUseCase @Inject constructor() {
    operator fun invoke(intensityString: String): IntensityLevel {
        return try {
            IntensityLevel.valueOf(intensityString.uppercase())
        } catch (e: Exception) {
            IntensityLevel.LOW
        }
    }
}
