package com.yuika.healthtracker.ui.features.main_features.add_meal

import com.yuika.healthtracker.domain.model.FoodCatalog
import com.yuika.healthtracker.domain.usecase.main_use_cases.food.SearchFoodCatalogUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.food.ValidateAndSaveMealUseCase
import com.yuika.healthtracker.service.widget.WidgetService
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class AddMealViewModel @Inject constructor(
    private val validateAndSaveMealUseCase: ValidateAndSaveMealUseCase,
    private val searchFoodCatalogUseCase: SearchFoodCatalogUseCase,
    private val widgetService: WidgetService
) : BaseViewModel<AddMealUiState, AddMealIntent, AddMealEffect>(
    initialState = AddMealUiState()
)
{
    override fun onIntent(intent: AddMealIntent)
    {
        when (intent)
        {
            is AddMealIntent.Init ->
            {
                updateState { it.copy(mealType = intent.mealType, dateText = intent.dateText) }
            }

            is AddMealIntent.OnFoodNameChange ->
            {
                handleFoodNameChange(intent.name)
            }

            is AddMealIntent.OnQuantityChange ->
            {
                handleQuantityChange(intent.quantity)
            }

            is AddMealIntent.OnUnitChange ->
            {
                updateState { it.copy(unit = intent.unit) }
            }

            is AddMealIntent.OnCaloriesChange ->
            {
                updateState { it.copy(calories = intent.calories, caloriesError = null) }
            }

            is AddMealIntent.OnMealTypeChange ->
            {
                updateState { it.copy(mealType = intent.mealType) }
            }

            is AddMealIntent.OnAddFoodClick ->
            {
                handleAddFood()
            }

            is AddMealIntent.OnRemoveFoodClick ->
            {
                handleRemoveFood(intent.id)
            }

            is AddMealIntent.OnSaveMealClick ->
            {
                handleSaveMeal()
            }

            is AddMealIntent.OnManualChange ->
            {
                updateState {
                    it.copy(
                        isManual = !it.isManual,
                        selectedFoodCatalogId = null,
                        selectedCategoriesPerServing = 0,
                        selectedDefaultQuantity = 1f,
                        searchResults = emptyList()
                    )
                }
            }

            is AddMealIntent.OnFoodCatalogClick ->
            {
                handleFoodCatalogClick(intent.food)
            }
        }
    }

    private var searchJob: Job? = null

    private fun handleFoodNameChange(name: String)
    {
        updateState {
            it.copy(
                foodName = name,
                foodNameError = null,
                selectedFoodCatalogId = null,
                selectedCategoriesPerServing = 0,
                selectedDefaultQuantity = 1f,
                searchResults = emptyList()
            )
        }

        searchJob?.cancel()
        if (name.isBlank() || state.value.isManual) return

        searchJob = launchSafe {
            searchFoodCatalogUseCase(name).collectLatest { foods ->
                updateState {
                    it.copy(searchResults = foods.take(5))
                }
            }
        }
    }

    private fun handleQuantityChange(quantity: String)
    {
        val q = quantity.toFloatOrNull()
        updateState { s ->
            val kcal = if (!s.isManual && s.selectedFoodCatalogId != null && q != null && q > 0f)
            {
                ((q / s.selectedDefaultQuantity.coerceAtLeast(1f)) * s.selectedCategoriesPerServing)
                    .roundToInt()
                    .toString()
            }
            else s.calories

            s.copy(quantity = quantity, quantityError = null, calories = kcal, caloriesError = null)
        }
    }

    private fun handleFoodCatalogClick(food: FoodCatalog)
    {
        updateState {
            it.copy(
                isManual = false,
                selectedFoodCatalogId = food.id,
                selectedCategoriesPerServing = food.caloriesPerServing,
                selectedDefaultQuantity = food.defaultQuantity,
                foodName = food.name,
                quantity = food.defaultQuantity.toString().removeSuffix(".0"),
                unit = food.unit,
                calories = food.caloriesPerServing.toString(),
                searchResults = emptyList(),
                foodNameError = null,
                quantityError = null,
                caloriesError = null
            )
        }
    }

    private fun handleAddFood()
    {
        val currentState = state.value

        val foodName = currentState.foodName.trim()
        val quantity = currentState.quantity.toFloatOrNull()
        val calories = currentState.calories.toIntOrNull()

        val foodNameError = if (foodName.isEmpty()) "Food name could not be blank" else null
        val quantityError = when
        {
            quantity == null -> "Quantity could not be blank"
            quantity <= 0f -> "Quantity must be greater than 0"
            else -> null
        }
        val caloriesError = when
        {
            calories == null -> "Please fill in the valid calories"
            calories < 0 -> "Please fill in the valid calories"
            else -> null
        }

        val hasError = foodNameError != null || quantityError != null || caloriesError != null

        if (hasError)
        {
            updateState {
                it.copy(
                    foodNameError = foodNameError,
                    quantityError = quantityError,
                    caloriesError = caloriesError,
                    errorMessage = null
                )
            }
            return
        }

        val newFood =
            AddedFoodItem(
                foodCatalogId = currentState.selectedFoodCatalogId,
                foodName = foodName,
                quantity = quantity ?: return,
                unit = currentState.unit,
                calories = calories ?: return,
                caloriesPerServing = if (currentState.selectedFoodCatalogId != null) currentState.selectedCategoriesPerServing
                else calories ?: return
            )

        val newAddedFoods = currentState.addedFoods + newFood

        updateState {
            it.copy(
                addedFoods = newAddedFoods,
                totalCalories = newAddedFoods.sumOf { item -> item.calories },
                foodName = "",
                quantity = "",
                calories = "",
                foodNameError = null,
                quantityError = null,
                caloriesError = null,
                errorMessage = null
            )
        }
    }

    private fun handleRemoveFood(id: String)
    {
        val newAddedFoods = state.value.addedFoods.filter { it.id != id }
        updateState {
            it.copy(
                addedFoods = newAddedFoods,
                totalCalories = newAddedFoods.sumOf { item -> item.calories }
            )
        }
    }

    private fun handleSaveMeal()
    {
        val currentState = state.value
        val currentFoods = currentState.addedFoods

        if (currentFoods.isEmpty())
        {
            updateState { it.copy(errorMessage = "Please at least add one food in the meal") }
            return
        }

        updateState { it.copy(isLoading = true, errorMessage = null) }

        launchSafe(
            onError = { throwable ->
                val message = throwable.message ?: "Error saving meal"
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = message,
                        isSuccess = false
                    )
                }
            }
        ) {
            validateAndSaveMealUseCase(
                currentFoods = currentFoods.map { it.toMealFoodInput() },
                dateText = currentState.dateText,
                mealType = currentState.mealType
            )

            widgetService.refresh()

            updateState {
                it.copy(
                    isLoading = false,
                    isSuccess = true,
                    addedFoods = emptyList(),
                    totalCalories = 0
                )
            }

            sendEffect(AddMealEffect.NavigateBackWithSuccess)
        }
    }
}
