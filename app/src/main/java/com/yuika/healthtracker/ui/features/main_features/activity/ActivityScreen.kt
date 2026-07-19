package com.yuika.healthtracker.ui.features.main_features.activity

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.yuika.healthtracker.ui.core.components.ErrorText
import com.yuika.healthtracker.ui.core.components.LoadingIndicator
import com.yuika.healthtracker.ui.core.components.SuccessText
import com.yuika.healthtracker.ui.features.main_features.activity.components.ActivityItem
import com.yuika.healthtracker.ui.features.main_features.activity.components.ActivitySummaryCard
import com.yuika.healthtracker.ui.theme.LocalSpacing
import com.yuika.healthtracker.utils.DATE_FORMATTER

@Composable
fun ActivityScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: ActivityViewModel = hiltViewModel(),
    onAddActivityClick: (String) -> Unit = {}
)
{
    val spacing = LocalSpacing.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(Unit) {
        viewModel.onIntent(ActivityIntent.LoadActivityData())
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect)
                {
                    is ActivityEffect.NavigateToAddActivity ->
                    {
                        onAddActivityClick(effect.dateText)
                    }
                }
            }
        }
    }

    state.selectedDetail?.let { activity ->
        AlertDialog(
            onDismissRequest = { viewModel.onIntent(ActivityIntent.DismissDetail) },
            title = { Text(activity.title) },
            text = {
                Column {
                    Text("Duration: ${activity.durationMins} mins")
                    Text("MET: ${activity.met}")
                    Text("Weight: ${activity.weightKg} kg")
                    Text("Burned: ${activity.kcal} kcal", fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.onIntent(ActivityIntent.DeleteActivityClick(activity.id)) }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onIntent(ActivityIntent.DismissDetail) }) {
                    Text("Cancel")
                }
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = spacing.large)
        ) {
            item {
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

                if (state.errorMessage != null)
                {
                    ErrorText(msg = state.errorMessage!!)
                }

                ActivitySummaryCard(
                    burnedKcal = state.burnedKcal,
                    goalKcal = state.goalKcal
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            if (state.isLoading)
            {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator()
                    }
                }
            }
            else
            {
                if (state.activities.isNotEmpty())
                {
                    itemsIndexed(
                        items = state.activities,
                        key = { index, activity ->
                            "${activity.title}-${activity.durationMins}-${activity.kcal}-$index"
                        }
                    ) { index, activity ->
                        ActivityItem(
                            activity = activity,
                            onClick = { viewModel.onIntent(ActivityIntent.ActivityClick(activity)) }
                        )
                        if (index < state.activities.lastIndex)
                        {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = DividerDefaults.Thickness,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                            )
                        }
                    }
                }
                else
                {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No activity today",
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }

        FloatingActionButton(
            onClick = { viewModel.onIntent(ActivityIntent.OnAddActivityClick) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(spacing.large),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.background
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Activity")
        }
    }
}
