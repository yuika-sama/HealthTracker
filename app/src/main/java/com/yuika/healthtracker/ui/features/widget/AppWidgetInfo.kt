package com.yuika.healthtracker.ui.features.widget

import android.content.Context
import android.content.res.Configuration
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.provideContent
import com.yuika.healthtracker.domain.model.ThemeMode
import com.yuika.healthtracker.service.widget.WidgetService
import java.util.Locale

class RefreshWidgetAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        WidgetService.from(context).refresh()
    }
}

fun Context.createContextWithLanguage(language: String): Context {
    val locale = Locale(language)
    val config = Configuration(resources.configuration)
    config.setLocale(locale)
    return createConfigurationContext(config)
}

class AppWidgetInfo : GlanceAppWidget()
{
    override suspend fun provideGlance(context: Context, id: GlanceId)
    {
        val state = WidgetService.from(context).loadWidgetState()
        val isSystemDark = (
                context.resources.configuration.uiMode and
                        Configuration.UI_MODE_NIGHT_MASK
                ) == Configuration.UI_MODE_NIGHT_YES
        val darkTheme = when (state.settings.themeMode)
        {
            ThemeMode.DARK -> true
            ThemeMode.LIGHT -> false
            ThemeMode.SYSTEM -> isSystemDark
        }
        val langContext = context.createContextWithLanguage(state.settings.language.localeTag)
        val strings = caloriesWidgetStrings(langContext, state.calories)
        provideContent {
            CaloriesWidgetContent(
                state = state.calories,
                darkTheme = darkTheme,
                colorPreset = state.settings.themeColorPreset,
                strings = strings
            )
        }
    }
}
