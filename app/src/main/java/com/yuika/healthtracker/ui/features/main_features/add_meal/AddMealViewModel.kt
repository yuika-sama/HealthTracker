package com.yuika.healthtracker.ui.features.main_features.add_meal

import com.yuika.healthtracker.R
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
                updateState { it.copy(calories = intent.calories, caloriesErrorRes = null) }
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
                foodNameErrorRes = null,
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

            s.copy(quantity = quantity, quantityErrorRes = null, calories = kcal, caloriesErrorRes = null)
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
                foodNameErrorRes = null,
                quantityErrorRes = null,
                caloriesErrorRes = null
            )
        }
    }

    private fun handleAddFood()
    {
        val currentState = state.value

        val foodName = currentState.foodName.trim()
        val quantity = currentState.quantity.toFloatOrNull()
        val calories = currentState.calories.toIntOrNull()

        val foodNameError = if (foodName.isEmpty()) R.string.error_food_name_blank else null
        val quantityError = when
        {
            quantity == null -> R.string.error_quantity_blank
            quantity <= 0f -> R.string.error_quantity_positive
            else -> null
        }
        val caloriesError = when
        {
            calories == null -> R.string.error_valid_calories
            calories < 0 -> R.string.error_valid_calories
            else -> null
        }

        val hasError = foodNameError != null || quantityError != null || caloriesError != null

        if (hasError)
        {
            updateState {
                it.copy(
                    foodNameErrorRes = foodNameError,
                    quantityErrorRes = quantityError,
                    caloriesErrorRes = caloriesError,
                    errorMessageRes = null
                )
            }
            return
        }

        val validQuantity = quantity ?: return
        val validCalories = calories ?: return
        val newFood =
            AddedFoodItem(
                foodCatalogId = currentState.selectedFoodCatalogId,
                foodName = foodName,
                quantity = validQuantity,
                unit = currentState.unit,
                calories = validCalories,
                caloriesPerServing = if (currentState.selectedFoodCatalogId != null) currentState.selectedCategoriesPerServing
                else validCalories
            )

        val newAddedFoods = currentState.addedFoods + newFood

        updateState {
            it.copy(
                addedFoods = newAddedFoods,
                totalCalories = newAddedFoods.sumOf { item -> item.calories },
                foodName = "",
                quantity = "",
                calories = "",
                foodNameErrorRes = null,
                quantityErrorRes = null,
                caloriesErrorRes = null,
                errorMessageRes = null
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
            updateState { it.copy(errorMessageRes = R.string.error_need_one_food) }
            return
        }

        updateState { it.copy(isLoading = true, errorMessageRes = null) }

        launchSafe(
            onError = {
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessageRes = R.string.error_save_meal,
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
                    totalCalories = 0,
                    errorMessageRes = null
                )
            }

            sendEffect(AddMealEffect.NavigateBackWithSuccess)
        }
    }
}
