package com.yuika.healthtracker.ui.features.main_features.onboarding

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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.yuika.healthtracker.ui.features.main_features.onboarding.components.MacroCard
import com.yuika.healthtracker.ui.features.main_features.onboarding.components.NutritionCard
import com.yuika.healthtracker.ui.features.main_features.onboarding.components.TargetOverviewCard
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

val proteinTarget = NutritionTarget(
    title = "Protein",
    amount = "165g",
    percentage = 30,
    icon = Icons.Default.Egg,
    color = InfoBlue
)

val fatsTarget = NutritionTarget(
    title = "Healthy Fats",
    amount = "61g",
    percentage = 25,
    icon = Icons.Default.WaterDrop,
    color = EnergyAmber
)

val carbsTarget = NutritionTarget(
    title = "Complex Carbs",
    amount = "248g",
    percentage = 45,
    icon = Icons.Outlined.BreakfastDining,
    color = Emerald
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingPage4Screen(
    onFinishClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Step 4 of 4",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Goal Calculated",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onFinishClick,
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
                        Text("Start My Journey", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "You can adjust these goals anytime in settings.",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

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
                text = "You're Ready to Go!",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Subtitle
            Text(
                text = "Based on your activity level and metabolic profile, we've calculated your optimal daily maintenance and nutrition targets.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Goal Cards
            TargetOverviewCard(
                calories = "2,200",
                bmr = "1,745 kcal",
                activityMultiplier = "1.2x Active"
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
                subtitle = "Daily Allotment"
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
                Text(
                    text = "\"Every great journey begins with a single, calculated step.\"",
                    style = MaterialTheme.typography.titleMedium.copy(fontStyle = FontStyle.Italic),
                    color = Color.White,
                    modifier = Modifier.padding(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPage4ScreenPreview() {
    OnboardingPage4Screen()
}
