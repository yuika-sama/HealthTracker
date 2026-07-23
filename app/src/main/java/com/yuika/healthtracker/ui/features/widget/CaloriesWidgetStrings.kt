package com.yuika.healthtracker.ui.features.widget

import android.content.Context
import com.yuika.healthtracker.R
import com.yuika.healthtracker.service.widget.WidgetCaloriesState

internal data class CaloriesWidgetStrings(
    val appName: String,
    val setupProfile: String,
    val today: String,
    val calories: String,
    val status: String,
    val headline: String,
    val progress: String,
    val eaten: String,
    val eatenValue: String,
    val burned: String,
    val burnedValue: String,
    val target: String,
    val targetValue: String,
    val balance: String,
    val balanceValue: String
)

internal fun caloriesWidgetStrings(
    context: Context,
    state: WidgetCaloriesState
): CaloriesWidgetStrings {
    val isOverTarget = state.remainingCalories < 0
    return CaloriesWidgetStrings(
        appName = context.getString(R.string.app_name),
        setupProfile = context.getString(R.string.widget_setup_profile),
        today = context.getString(R.string.date_today_caps),
        calories = context.getString(R.string.label_calories),
        status = context.getString(if (isOverTarget) R.string.widget_over_target else R.string.widget_on_track),
        headline = context.getString(
            if (isOverTarget) R.string.widget_kcal_over else R.string.widget_kcal_left,
            kotlin.math.abs(state.remainingCalories)
        ),
        progress = context.getString(R.string.widget_kcal_target, state.progressPercent, state.targetCalories),
        eaten = context.getString(R.string.stat_eaten),
        eatenValue = context.getString(R.string.widget_kcal_value, state.eatenCalories),
        burned = context.getString(R.string.stat_burned),
        burnedValue = context.getString(R.string.widget_kcal_value, state.burnedCalories),
        target = context.getString(R.string.widget_target),
        targetValue = context.getString(R.string.widget_kcal_value, state.targetCalories),
        balance = context.getString(R.string.widget_balance),
        balanceValue = context.getString(R.string.widget_kcal_value, state.balanceCalories)
    )
}
