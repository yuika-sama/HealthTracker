package com.yuika.healthtracker.ui.features.main_features.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yuika.healthtracker.R

@Composable
fun DashboardBottomNav(
    modifier: Modifier = Modifier,
    currentRoute: String = "home",
    onTabClick: (String) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DashboardBottomNavItem(
            icon = Icons.Outlined.GridView,
            label = stringResource(R.string.nav_home),
            isSelected = currentRoute == "home",
            onClick = { onTabClick("home") }
        )
        DashboardBottomNavItem(
            icon = Icons.Outlined.Restaurant,
            label = stringResource(R.string.nav_diary),
            isSelected = currentRoute == "diary",
            onClick = { onTabClick("diary") }
        )
        DashboardBottomNavItem(
            icon = Icons.Outlined.FitnessCenter,
            label = stringResource(R.string.nav_activity),
            isSelected = currentRoute == "activity",
            onClick = { onTabClick("activity") }
        )
        DashboardBottomNavItem(
            icon = Icons.Outlined.BarChart,
            label = stringResource(R.string.nav_trends),
            isSelected = currentRoute == "trends",
            onClick = { onTabClick("trends") }
        )
        DashboardBottomNavItem(
            icon = Icons.Outlined.Person,
            label = stringResource(R.string.nav_profile),
            isSelected = currentRoute == "profile",
            onClick = { onTabClick("profile") }
        )
    }
}
