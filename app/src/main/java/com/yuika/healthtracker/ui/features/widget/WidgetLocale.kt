package com.yuika.healthtracker.ui.features.widget

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

fun createContextWithLanguage(context: Context, language: String): Context {
    val locale = Locale.forLanguageTag(language)
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    return context.createConfigurationContext(config)
}
