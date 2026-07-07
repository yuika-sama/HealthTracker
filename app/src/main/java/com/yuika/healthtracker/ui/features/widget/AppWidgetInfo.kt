package com.yuika.healthtracker.ui.features.widget

import android.content.Context
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

class AppWidgetInfo : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
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
                        text = "Calories progress",
                        style = TextStyle(
                            color = GlanceTheme.colors.onSurface
                        )
                    )
                    Text(text = "1500/2000 kcal")
                }
            }
        }
    }
}