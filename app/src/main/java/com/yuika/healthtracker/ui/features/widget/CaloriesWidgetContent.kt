package com.yuika.healthtracker.ui.features.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.yuika.healthtracker.R
import com.yuika.healthtracker.domain.model.ThemeColorPreset
import com.yuika.healthtracker.service.widget.WidgetCaloriesState
import com.yuika.healthtracker.ui.theme.BackgroundDark
import com.yuika.healthtracker.ui.theme.BackgroundLight
import com.yuika.healthtracker.ui.theme.EnergyAmber
import com.yuika.healthtracker.ui.theme.ErrorRed
import com.yuika.healthtracker.ui.theme.InfoBlue
import com.yuika.healthtracker.ui.theme.OnSurfaceDark
import com.yuika.healthtracker.ui.theme.OnSurfaceLight
import com.yuika.healthtracker.ui.theme.OnSurfaceVariantDark
import com.yuika.healthtracker.ui.theme.OnSurfaceVariantLight
import com.yuika.healthtracker.ui.theme.OutlineDark
import com.yuika.healthtracker.ui.theme.OutlineLight
import com.yuika.healthtracker.ui.theme.SurfaceContainerDark
import com.yuika.healthtracker.ui.theme.SurfaceContainerLight
import com.yuika.healthtracker.ui.theme.accentColors

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

@Composable
internal fun CaloriesWidgetContent(
    state: WidgetCaloriesState,
    darkTheme: Boolean,
    colorPreset: ThemeColorPreset,
    strings: CaloriesWidgetStrings
) {
    val accent = colorPreset.accentColors()
    val backgroundColor = if (darkTheme) BackgroundDark else BackgroundLight
    val cardColor = if (darkTheme) SurfaceContainerDark else SurfaceContainerLight
    val textColor = if (darkTheme) OnSurfaceDark else OnSurfaceLight
    val mutedTextColor = if (darkTheme) OnSurfaceVariantDark else OnSurfaceVariantLight
    val progressBackground = if (darkTheme) OutlineDark else OutlineLight
    val isOverTarget = state.remainingCalories < 0
    val mainColor = if (isOverTarget) ErrorRed else accent.secondary

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(backgroundColor)
            .cornerRadius(24.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start
    ) {
        if (!state.hasUser) {
            Text(
                text = strings.appName,
                style = TextStyle(
                    color = accent.secondary.toGlanceColorProvider(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = GlanceModifier.height(8.dp))
            Text(
                text = strings.setupProfile,
                style = TextStyle(
                    color = mutedTextColor.toGlanceColorProvider(),
                    fontSize = 13.sp
                )
            )
        } else {
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = GlanceModifier.defaultWeight()) {
                    Text(
                        text = strings.today,
                        style = TextStyle(
                            color = mutedTextColor.toGlanceColorProvider(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = strings.calories,
                        style = TextStyle(
                            color = textColor.toGlanceColorProvider(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Text(
                    text = strings.status,
                    style = TextStyle(
                        color = mainColor.toGlanceColorProvider(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = GlanceModifier.height(10.dp))

            Text(
                text = strings.headline,
                style = TextStyle(
                    color = mainColor.toGlanceColorProvider(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = strings.progress,
                style = TextStyle(
                    color = mutedTextColor.toGlanceColorProvider(),
                    fontSize = 12.sp
                )
            )

            Spacer(modifier = GlanceModifier.height(10.dp))

            LinearProgressIndicator(
                progress = state.progress,
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(9.dp),
                color = mainColor.toGlanceColorProvider(),
                backgroundColor = progressBackground.copy(alpha = 0.32f).toGlanceColorProvider()
            )

            Spacer(modifier = GlanceModifier.height(12.dp))

            Row(modifier = GlanceModifier.fillMaxWidth()) {
                WidgetMetricCard(strings.eaten, strings.eatenValue, InfoBlue, cardColor, textColor)
                Spacer(modifier = GlanceModifier.width(8.dp))
                WidgetMetricCard(strings.burned, strings.burnedValue, EnergyAmber, cardColor, textColor)
            }

            Spacer(modifier = GlanceModifier.height(8.dp))

            Row(modifier = GlanceModifier.fillMaxWidth()) {
                WidgetMetricCard(strings.target, strings.targetValue, accent.secondary, cardColor, textColor)
                Spacer(modifier = GlanceModifier.width(8.dp))
                WidgetMetricCard(strings.balance, strings.balanceValue, mainColor, cardColor, textColor)
            }
        }
    }
}
