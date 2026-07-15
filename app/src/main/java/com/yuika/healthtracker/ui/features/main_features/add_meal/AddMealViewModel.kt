package com.yuika.healthtracker.ui.features.main_features.add_meal

import com.yuika.healthtracker.domain.usecase.main_use_cases.food.ValidateAndSaveMealUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.utils.NETWORK_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class AddMealViewModel @Inject constructor(
    private val validateAndSaveMealUseCase: ValidateAndSaveMealUseCase
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
                updateState { it.copy(foodName = intent.name, foodNameError = null) }
            }

            is AddMealIntent.OnQuantityChange ->
            {
                updateState { it.copy(quantity = intent.quantity, quantityError = null) }
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
            TempFoodItem(
                foodName = foodName,
                quantity = quantity ?: return,
                unit = currentState.unit,
                calories = calories ?: return
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
            sendEffect(AddMealEffect.ShowError("Please at least add one food in the meal"))
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
                sendEffect(AddMealEffect.ShowError(message))
            }
        ) {
            validateAndSaveMealUseCase(
                currentFoods = currentFoods,
                dateText = currentState.dateText,
                mealType = currentState.mealType
            )

            delay(NETWORK_DELAY.toLong())
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