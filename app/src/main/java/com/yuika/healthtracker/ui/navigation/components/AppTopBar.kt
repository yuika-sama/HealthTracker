package com.yuika.healthtracker.ui.navigation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yuika.healthtracker.ui.features.main_features.dashboard.components.DashboardTopBar

@Composable
fun AppTopBar(
    showMainBar: Boolean,
    title: String?,
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        showMainBar -> DashboardTopBar(modifier = modifier)
        title != null -> DetailTopBar(
            title = title,
            showBackButton = showBackButton,
            onBackClick = onBackClick,
            modifier = modifier
        )
    }
}
