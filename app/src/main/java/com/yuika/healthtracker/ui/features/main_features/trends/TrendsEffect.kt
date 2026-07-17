package com.yuika.healthtracker.ui.features.main_features.trends

import android.net.Uri
import com.yuika.healthtracker.ui.core.base.UiEffect

sealed class TrendsEffect: UiEffect {
    data class ShareWeeklyReport(val uri: Uri): TrendsEffect()
}
