package com.yuika.healthtracker.ui.navigation.components

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppTopBar(
    showMainBar: Boolean,
    title: String?,
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val safeModifier = modifier.statusBarsPadding()

    when {
        showMainBar -> MainTopBar(modifier = safeModifier)
        title != null -> DetailTopBar(
            title = title,
            showBackButton = showBackButton,
            onBackClick = onBackClick,
            modifier = safeModifier
        )
    }
}
