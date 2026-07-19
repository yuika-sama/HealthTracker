package com.yuika.healthtracker.ui.features.widget

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
import androidx.glance.unit.ColorProvider
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

@Composable
fun CaloriesWidgetContent(
    state: WidgetCaloriesState,
    darkTheme: Boolean,
    colorPreset: ThemeColorPreset
) {
    val accent = colorPreset.accentColors()
    val backgroundColor = if (darkTheme) BackgroundDark else BackgroundLight
    val cardColor = if (darkTheme) SurfaceContainerDark else SurfaceContainerLight
    val textColor = if (darkTheme) OnSurfaceDark else OnSurfaceLight
    val mutedTextColor = if (darkTheme) OnSurfaceVariantDark else OnSurfaceVariantLight
    val progressBackground = if (darkTheme) OutlineDark else OutlineLight
    val isOverTarget = state.remainingCalories < 0
    val mainColor = if (isOverTarget) ErrorRed else accent.secondary
    val headline = if (isOverTarget) {
        "${-state.remainingCalories} kcal over"
    } else {
        "${state.remainingCalories} kcal left"
    }
    val status = if (isOverTarget) {
        "OVER TARGET"
    } else {
        "ON TRACK"
    }

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
                text = "Health Tracker",
                style = TextStyle(
                    color = ColorProvider(accent.secondary),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = GlanceModifier.height(8.dp))
            Text(
                text = "Set up your profile to see today's calories.",
                style = TextStyle(
                    color = ColorProvider(mutedTextColor),
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
                        text = "TODAY",
                        style = TextStyle(
                            color = ColorProvider(mutedTextColor),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = "Calories",
                        style = TextStyle(
                            color = ColorProvider(textColor),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Text(
                    text = status,
                    style = TextStyle(
                        color = ColorProvider(mainColor),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = GlanceModifier.height(10.dp))

            Text(
                text = headline,
                style = TextStyle(
                    color = ColorProvider(mainColor),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "${state.progressPercent}% of ${state.targetCalories} kcal target",
                style = TextStyle(
                    color = ColorProvider(mutedTextColor),
                    fontSize = 12.sp
                )
            )

            Spacer(modifier = GlanceModifier.height(10.dp))

            LinearProgressIndicator(
                progress = state.progress,
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(9.dp),
                color = ColorProvider(mainColor),
                backgroundColor = ColorProvider(progressBackground.copy(alpha = 0.32f))
            )

            Spacer(modifier = GlanceModifier.height(12.dp))

            Row(modifier = GlanceModifier.fillMaxWidth()) {
                WidgetMetricCard("Eaten", "${state.eatenCalories} kcal", InfoBlue, cardColor, textColor)
                Spacer(modifier = GlanceModifier.width(8.dp))
                WidgetMetricCard("Burned", "${state.burnedCalories} kcal", EnergyAmber, cardColor, textColor)
            }

            Spacer(modifier = GlanceModifier.height(8.dp))

            Row(modifier = GlanceModifier.fillMaxWidth()) {
                WidgetMetricCard("Target", "${state.targetCalories} kcal", accent.secondary, cardColor, textColor)
                Spacer(modifier = GlanceModifier.width(8.dp))
                WidgetMetricCard("Balance", "${state.balanceCalories} kcal", mainColor, cardColor, textColor)
            }
        }
    }
}
