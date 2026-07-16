package com.yuika.healthtracker.ui.features.main_features.add_meal

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.yuika.healthtracker.ui.core.components.ErrorText
import com.yuika.healthtracker.ui.core.components.LoadingIndicator
import com.yuika.healthtracker.ui.core.components.SuccessText
import com.yuika.healthtracker.ui.features.main_features.add_meal.components.AddFoodFormCard
import com.yuika.healthtracker.ui.features.main_features.add_meal.components.AddedFoodItemCard
import com.yuika.healthtracker.ui.features.main_features.add_meal.components.DashedAddButton
import com.yuika.healthtracker.ui.theme.LocalSpacing
import com.yuika.healthtracker.utils.getMealIntentForCurrentTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealScreen(
    modifier: Modifier = Modifier,
    mealType: String,
    dateText: String,
    viewModel: AddMealViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
)
{
    val spacing = LocalSpacing.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(mealType, dateText) {
        viewModel.onIntent(AddMealIntent.Init(mealType, dateText = dateText))
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
            viewModel.effect.collect { effect ->
                when (effect) {
                    is AddMealEffect.NavigateBackWithSuccess -> {
                        Toast.makeText(context, "Save success", Toast.LENGTH_SHORT).show()
                        onSaveClick()
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Add a meal",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(end = 48.dp)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                        navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
                HorizontalDivider(
                    Modifier,
                    DividerDefaults.Thickness,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.05f)
                )
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                HorizontalDivider(
                    Modifier,
                    DividerDefaults.Thickness,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                )

                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "TOTAL MEAL",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                            Text(
                                text = "${state.totalCalories} kc",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "MEAL",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                            Text(
                                text = state.mealType.ifEmpty { "Lunch" },
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {viewModel.onIntent(AddMealIntent.OnSaveMealClick)},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = Color.White
                        ),
                        enabled = !state.isLoading
                    ) {
                        if (state.isLoading){
                            LoadingIndicator()
                        } else {
                            Text(
                                text = "Save your meal",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = spacing.large),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                state.errorMessage?.let { ErrorText(msg = it) }

                if (state.isSuccess && !state.isLoading && state.errorMessage == null) {
                    SuccessText(msg = "Meal saved")
                }

                AddFoodFormCard(
                    state = state,
                    onFoodNameChange = {
                        viewModel.onIntent(AddMealIntent.OnFoodNameChange(it))
                    },
                    onQuantityChange = { viewModel.onIntent(AddMealIntent.OnQuantityChange(it)) },
                    onUnitChange = { viewModel.onIntent(AddMealIntent.OnUnitChange(it)) },
                    onCaloriesChange = { viewModel.onIntent(AddMealIntent.OnCaloriesChange(it)) },
                    onMealTypeChange = { viewModel.onIntent(AddMealIntent.OnMealTypeChange(it)) },
                    onManualChange = {viewModel.onIntent(AddMealIntent.OnManualChange)},
                    onFoodCatalogClick = {viewModel.onIntent(AddMealIntent.OnFoodCatalogClick(it))}
                )
            }

            items(state.addedFoods, key = { it.id }) { food ->
                AddedFoodItemCard(
                    foodName = food.foodName,
                    quantityInfo = "${food.quantity} ${food.unit}",
                    calories = "${food.calories} kcal",
                    onRemoveClick = { viewModel.onIntent(AddMealIntent.OnRemoveFoodClick(food.id)) }
                )
            }

            item {
                DashedAddButton(onClick = { viewModel.onIntent(AddMealIntent.OnAddFoodClick ) })
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
