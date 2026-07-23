package com.yuika.healthtracker.ui.navigation.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppBottomBar(
    currentTab: String?,
    onTabClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (currentTab != null) {
        AppBottomNav(
            modifier = modifier.navigationBarsPadding(),
            currentRoute = currentTab,
            onTabClick = onTabClick
        )
    }
}
