package com.yuika.healthtracker.ui.features.main_features.diary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yuika.healthtracker.ui.features.main_features.dashboard.components.DashboardBottomNav
import com.yuika.healthtracker.ui.features.main_features.dashboard.components.DashboardTopBar
import com.yuika.healthtracker.ui.features.main_features.diary.components.DailyStats
import com.yuika.healthtracker.ui.features.main_features.diary.components.DateSelector
import com.yuika.healthtracker.ui.features.main_features.diary.components.FoodItem
import com.yuika.healthtracker.ui.features.main_features.diary.components.MealCard
import com.yuika.healthtracker.ui.theme.Emerald
import com.yuika.healthtracker.ui.theme.LocalSpacing

@Composable
fun DiaryScreen(
    modifier: Modifier = Modifier,
    onAddFoodClick: (String) -> Unit = {},
    onTabClick: (String) -> Unit = {}
) {
    val spacing = LocalSpacing.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            DashboardTopBar()
        },
        bottomBar = {
            DashboardBottomNav(currentRoute = "diary", onTabClick = onTabClick)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddFoodClick("Breakfast") },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.background
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
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
            
            DateSelector()
            
            DailyStats()
            
            MealCard(
                title = "Breakfast",
                recommendedKcal = "Recommended: 400-500 kcal",
                totalKcal = 350,
                icon = Icons.Outlined.WbSunny,
                foods = listOf(
                    FoodItem("Oatmeal with Berries", "1 bowl (250g)", 220),
                    FoodItem("Black Coffee", "1 cup (240ml)", 5),
                    FoodItem("Boiled Egg", "2 large (100g)", 125)
                ),
                onAddFoodClick = { onAddFoodClick("Breakfast") }
            )
            
            MealCard(
                title = "Lunch",
                recommendedKcal = "Recommended: 600-700 kcal",
                totalKcal = 650,
                icon = Icons.Outlined.WbSunny,
                foods = listOf(
                    FoodItem("Grilled Chicken Salad", "1 large bowl (350g)", 450),
                    FoodItem("Olive Oil Dressing", "2 tbsp (30ml)", 200)
                ),
                onAddFoodClick = { onAddFoodClick("Lunch") }
            )
            
            MealCard(
                title = "Dinner",
                recommendedKcal = "Recommended: 500-600 kcal",
                totalKcal = 0,
                icon = Icons.Outlined.DarkMode,
                foods = emptyList(), // Empty list will show "No foods logged yet"
                onAddFoodClick = { onAddFoodClick("Dinner") }
            )
            
            MealCard(
                title = "Snack",
                recommendedKcal = "Snacks & Drinks",
                totalKcal = 250,
                icon = Icons.Outlined.Coffee,
                foods = listOf(
                    FoodItem("Greek Yogurt", "1 container (150g)", 150),
                    FoodItem("Almonds", "Small handful (15g)", 100)
                ),
                onAddFoodClick = { onAddFoodClick("Snack") }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiaryScreenPreview() {
    DiaryScreen()
}
