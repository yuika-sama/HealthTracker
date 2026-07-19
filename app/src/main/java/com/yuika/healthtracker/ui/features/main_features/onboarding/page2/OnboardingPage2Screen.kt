package com.yuika.healthtracker.ui.features.main_features.onboarding.page2

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.yuika.healthtracker.ui.core.components.ErrorText
import com.yuika.healthtracker.ui.core.components.LoadingIndicator
import com.yuika.healthtracker.ui.features.main_features.onboarding.components.ActivityLevelCard

data class ActivityLevelOption(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector
)

val activityLevelOptions = listOf(
    ActivityLevelOption(
        id = "sedentary",
        title = "Sedentary",
        description = "Little to no exercise, desk job",
        icon = Icons.Default.Chair
    ),
    ActivityLevelOption(
        id = "lightly_active",
        title = "Lightly Active",
        description = "Light exercise/sports 1-3 days/week",
        icon = Icons.AutoMirrored.Filled.DirectionsWalk
    ),
    ActivityLevelOption(
        id = "moderately_active",
        title = "Moderately Active",
        description = "Moderate exercise/sports 3-5 days/week",
        icon = Icons.Default.FitnessCenter
    ),
    ActivityLevelOption(
        id = "very_active",
        title = "Very Active",
        description = "Hard exercise/sports 6-7 days a week",
        icon = Icons.AutoMirrored.Filled.DirectionsRun
    ),
    ActivityLevelOption(
        id = "extra_active",
        title = "Extra Active",
        description = "Very hard exercise & physical job",
        icon = Icons.Default.SportsMartialArts
    )
)

@Composable
fun OnboardingPage2Screen(
    viewModel: OnboardingPage2ViewModel = hiltViewModel(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onNavigateBack: () -> Unit = {},
    onNavigateNext: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
            viewModel.effect.collect { effect ->
                when (effect) {
                    is OnboardingPage2Effect.NavigateToPage3 -> onNavigateNext()
                    is OnboardingPage2Effect.NavigateBack -> onNavigateBack()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(contentPadding)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "Your Activity Level",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Text(
                    text = "Step 2 of 4",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f) // 50% for step 2
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Subtitle
            Text(
                text = "This helps us estimate your daily calorie burn and set accurate targets for your vitality journey.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Options List
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                activityLevelOptions.forEach { option ->
                    ActivityLevelCard(
                        icon = option.icon,
                        title = option.title,
                        description = option.description,
                        isSelected = state.activityLevel == option.id,
                        onClick = { viewModel.onIntent(OnboardingPage2Intent.ActivityLevelChanged(option.id)) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            state.errorMessage?.let {
                ErrorText(it)
            }

            Button(
                onClick = { viewModel.onIntent(OnboardingPage2Intent.Submit) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = !state.isLoading
            ) {
                if (state.isLoading){
                    LoadingIndicator()
                } else {
                    Text("Continue", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
}
