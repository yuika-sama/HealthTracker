package com.yuika.healthtracker.ui.features.main_features.activity

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.glance.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.yuika.healthtracker.ui.core.components.LoadingIndicator
import com.yuika.healthtracker.ui.features.main_features.activity.components.ActivityListCard
import com.yuika.healthtracker.ui.features.main_features.activity.components.ActivitySummaryCard
import com.yuika.healthtracker.ui.features.main_features.dashboard.components.DashboardBottomNav
import com.yuika.healthtracker.ui.features.main_features.dashboard.components.DashboardTopBar
import com.yuika.healthtracker.ui.theme.LocalSpacing
import com.yuika.healthtracker.utils.DATE_FORMATTER

@Composable
fun ActivityScreen(
    modifier: Modifier = Modifier,
    viewModel: ActivityViewModel = hiltViewModel(),
    onAddActivityClick: () -> Unit = {},
    onTabClick: (String) -> Unit = {}
)
{
    val spacing = LocalSpacing.current
    val scrollState = rememberScrollState()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(Unit) {
        viewModel.onIntent(ActivityIntent.LoadActivityData())
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect)
                {
                    is ActivityEffect.ShowError ->
                    {
                        Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                    }

                    is ActivityEffect.NavigateToAddActivity ->
                    {
                        onAddActivityClick()
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            DashboardTopBar()
        },
        bottomBar = {
            DashboardBottomNav(currentRoute = "activity", onTabClick = onTabClick)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onIntent(ActivityIntent.OnAddActivityClick) },
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
                text = state.selectedDate.format(DATE_FORMATTER),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            ActivitySummaryCard(
                burnedKcal = state.burnedKcal,
                goalKcal = state.goalKcal
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (state.isLoading)
            {
                LoadingIndicator()
            }
            else
            {
                if (state.activities.isNotEmpty())
                {
                    ActivityListCard(activities = state.activities)
                }
                else
                {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No activity today", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}