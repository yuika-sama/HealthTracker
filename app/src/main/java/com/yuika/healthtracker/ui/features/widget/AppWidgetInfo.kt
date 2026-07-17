package com.yuika.healthtracker.ui.features.widget

import android.content.Context
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.yuika.healthtracker.service.widget.WidgetService

class AppWidgetInfo : GlanceAppWidget()
{
    override suspend fun provideGlance(context: Context, id: GlanceId)
    {
        val state = WidgetService.from(context).loadTodayCalories()
        provideContent {
            GlanceTheme {
                Column(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(GlanceTheme.colors.surface)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Today calories",
                        style = TextStyle(
                            color = GlanceTheme.colors.onSurface
                        )
                    )
                    Spacer(modifier = GlanceModifier.height(8.dp))
                    if (!state.hasUser)
                    {
                        Text(text = "Set up profile first")
                    }
                    else
                    {
                        Text(
                            text = if (state.remainingCalories < 0)
                            {
                                "${-state.remainingCalories} kcal over"
                            }
                            else
                            {
                                "${state.remainingCalories} kcal left"
                            }
                        )

                        Spacer(modifier = GlanceModifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = state.progress,
                            modifier = GlanceModifier
                                .fillMaxWidth()
                                .height(8.dp)
                        )

                        Spacer(GlanceModifier.height(8.dp))

                        Text(text = "${state.progressPercent}% of ${state.targetCalories} kcal")
                        Text(text = "Eaten ${state.eatenCalories} | Burned ${state.burnedCalories}")
                    }
                }
            }
        }
    }
}