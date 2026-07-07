package com.yuika.healthtracker.ui.features.main_features.activity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yuika.healthtracker.ui.features.main_features.activity.components.ActivityItemData
import com.yuika.healthtracker.ui.features.main_features.activity.components.ActivityListCard
import com.yuika.healthtracker.ui.features.main_features.activity.components.ActivitySummaryCard
import com.yuika.healthtracker.ui.features.main_features.activity.components.IntensityLevel
import com.yuika.healthtracker.ui.features.main_features.dashboard.components.DashboardBottomNav
import com.yuika.healthtracker.ui.features.main_features.dashboard.components.DashboardTopBar
import com.yuika.healthtracker.ui.theme.Emerald
import com.yuika.healthtracker.ui.theme.LocalSpacing

@Composable
fun ActivityScreen(
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val scrollState = rememberScrollState()

    val activities = listOf(
        ActivityItemData(
            title = "Đi bộ buổi sáng",
            intensity = IntensityLevel.LOW,
            durationMins = 30,
            kcal = 120,
            iconType = IntensityLevel.LOW
        ),
        ActivityItemData(
            title = "Chạy bộ công viên",
            intensity = IntensityLevel.HIGH,
            durationMins = 45,
            kcal = 330,
            iconType = IntensityLevel.HIGH
        )
    )

    Scaffold(
        topBar = {
            DashboardTopBar()
        },
        bottomBar = {
            DashboardBottomNav(currentRoute = "activity")
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO */ },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.background
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Activity")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = spacing.large)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Today's Activity",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "Thursday, Oct 26",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            ActivitySummaryCard(
                burnedKcal = 450,
                goalKcal = 600
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            ActivityListCard(
                activities = activities
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActivityScreenPreview() {
    ActivityScreen()
}
