package com.yuika.healthtracker.ui.navigation.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yuika.healthtracker.ui.features.main_features.dashboard.components.DashboardBottomNav

@Composable
fun AppBottomBar(
    currentTab: String?,
    onTabClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (currentTab != null) {
        DashboardBottomNav(
            modifier = modifier.navigationBarsPadding(),
            currentRoute = currentTab,
            onTabClick = onTabClick
        )
    }
}
