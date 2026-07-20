package com.yuika.healthtracker.ui.features.main_features.onboarding.page4

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BreakfastDining
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil3.compose.AsyncImage
import com.yuika.healthtracker.R
import com.yuika.healthtracker.ui.core.components.ErrorText
import com.yuika.healthtracker.ui.core.components.LoadingIndicator
import com.yuika.healthtracker.ui.core.i18n.activityMultiplierLabel
import com.yuika.healthtracker.ui.features.main_features.onboarding.components.MacroCard
import com.yuika.healthtracker.ui.features.main_features.onboarding.components.NutritionCard
import com.yuika.healthtracker.ui.features.main_features.onboarding.components.TargetOverviewCard
import com.yuika.healthtracker.ui.features.main_features.onboarding.page3.OnboardingPage3Effect
import com.yuika.healthtracker.ui.theme.Emerald
import com.yuika.healthtracker.ui.theme.EnergyAmber
import com.yuika.healthtracker.ui.theme.InfoBlue

data class NutritionTarget(
    val title: String,
    val amount: String,
    val percentage: Int,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun OnboardingPage4Screen(
    viewModel: OnboardingPage4ViewModel = hiltViewModel(),
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
                    is OnboardingPage4Effect.NavigateToDashboard -> onNavigateNext()
                }
            }
        }
    }

    val proteinTarget = NutritionTarget(
        title = stringResource(R.string.onboarding_protein),
        amount = "${state.proteinGrams}g",
        percentage = 30,
        icon = Icons.Default.Egg,
        color = InfoBlue
    )

    val fatsTarget = NutritionTarget(
        title = stringResource(R.string.onboarding_healthy_fats),
        amount = "${state.fatGrams}g",
        percentage = 25,
        icon = Icons.Default.WaterDrop,
        color = EnergyAmber
    )

    val carbsTarget = NutritionTarget(
        title = stringResource(R.string.onboarding_carbs),
        amount = "${state.carbsGrams}g",
        percentage = 45,
        icon = Icons.Outlined.BreakfastDining,
        color = Emerald
    )

    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(contentPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.onboarding_step, 4, 4),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(MaterialTheme.colorScheme.primary)
                )
                
                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Celebration,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))

                // Header Section
                Text(
                    text = stringResource(R.string.onboarding_ready_title),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Subtitle
                Text(
                    text = stringResource(R.string.onboarding_ready_subtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
                
                Spacer(modifier = Modifier.height(32.dp))

                state.errorMessage?.let {
                    ErrorText(it)
                }
                
                // Goal Cards
                TargetOverviewCard(
                    calories = "${state.tdee}",
                    bmr = "${state.bmr} ${stringResource(R.string.unit_kcal)}",
                    activityMultiplier = activityMultiplierLabel(state.activityMultiplierText)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    NutritionCard(
                        modifier = Modifier.weight(1f),
                        title = proteinTarget.title,
                        amount = proteinTarget.amount,
                        percentage = proteinTarget.percentage,
                        icon = proteinTarget.icon,
                        color = proteinTarget.color
                    )
                    
                    NutritionCard(
                        modifier = Modifier.weight(1f),
                        title = fatsTarget.title,
                        amount = fatsTarget.amount,
                        percentage = fatsTarget.percentage,
                        icon = fatsTarget.icon,
                        color = fatsTarget.color
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                MacroCard(
                    title = carbsTarget.title,
                    amount = carbsTarget.amount,
                    percentage = carbsTarget.percentage,
                    icon = carbsTarget.icon,
                    color = carbsTarget.color,
                    subtitle = stringResource(R.string.onboarding_daily_allotment)
                )
                
                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.BottomStart
                ) {
                    AsyncImage(
                        model = "https://images.samsung.com/is/image/samsung/assets/apps/global/samsung-health/2025-07/samsung-health-sleep-overview-pc.jpg?imbypass=true",
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.72f)
                                    )
                                )
                            )
                    )
                    Text(
                        text = stringResource(R.string.onboarding_quote),
                        style = MaterialTheme.typography.titleMedium.copy(fontStyle = FontStyle.Italic),
                        color = Color.White,
                        modifier = Modifier.padding(20.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.onIntent(OnboardingPage4Intent.CompleteOnboarding) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Text(stringResource(R.string.onboarding_start_journey), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.onboarding_adjust_settings),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
    }
}
