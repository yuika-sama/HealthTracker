package com.yuika.healthtracker.ui.features.widget

import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.RowScope
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

@Composable
fun RowScope.WidgetMetricCard(
    label: String,
    value: String,
    color: Color,
    backgroundColor: Color,
    textColor: Color
) {
    Column(
        modifier = GlanceModifier
            .defaultWeight()
            .background(backgroundColor)
            .cornerRadius(16.dp)
            .padding(10.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                color = ColorProvider(color),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        )
        Text(
            text = value,
            style = TextStyle(
                color = ColorProvider(textColor),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}
