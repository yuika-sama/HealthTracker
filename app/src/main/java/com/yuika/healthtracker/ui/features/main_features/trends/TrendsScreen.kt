package com.yuika.healthtracker.ui.features.main_features.trends

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yuika.healthtracker.ui.features.main_features.dashboard.components.DashboardBottomNav
import com.yuika.healthtracker.ui.features.main_features.dashboard.components.DashboardTopBar
import com.yuika.healthtracker.ui.features.main_features.trends.components.CalorieIntakeChart
import com.yuika.healthtracker.ui.features.main_features.trends.components.NetCaloriesChart
import com.yuika.healthtracker.ui.features.main_features.trends.components.TimeRangeSelector
import com.yuika.healthtracker.ui.features.main_features.trends.components.TrendStatCard
import com.yuika.healthtracker.ui.theme.Emerald
import com.yuika.healthtracker.ui.theme.InfoBlue
import com.yuika.healthtracker.ui.theme.LocalSpacing

@Composable
fun TrendsScreen(
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            DashboardTopBar()
        },
        bottomBar = {
            DashboardBottomNav(currentRoute = "trends")
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = spacing.large)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            
            Column {
                Text(
                    text = "Statistics & Trends",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Track your progress and analyze your habits.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
            
            TimeRangeSelector(
                selectedRange = "Week"
            )
            
            TrendStatCard(
                title = "Avg Intake / Day",
                value = "2,150",
                valueColor = MaterialTheme.colorScheme.secondary,
                unit = "kcal"
            )
            
            TrendStatCard(
                title = "Avg Burned / Day",
                value = "640",
                valueColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                unit = "kcal"
            )
            
            TrendStatCard(
                title = "Days Meeting Goal",
                value = "5",
                valueColor = MaterialTheme.colorScheme.tertiary,
                unit = "/ 7 days"
            )
            
            CalorieIntakeChart()
            
            NetCaloriesChart()
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TrendsScreenPreview() {
    TrendsScreen()
}
