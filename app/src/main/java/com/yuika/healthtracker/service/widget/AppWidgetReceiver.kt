package com.yuika.healthtracker.service.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.yuika.healthtracker.ui.features.widget.AppWidgetInfo

class AppWidgetReceiver : GlanceAppWidgetReceiver()
{
    override val glanceAppWidget: GlanceAppWidget = AppWidgetInfo()
}