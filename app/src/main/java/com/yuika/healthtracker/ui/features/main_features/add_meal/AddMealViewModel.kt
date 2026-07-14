package com.yuika.healthtracker.ui.features.main_features.add_meal

import com.yuika.healthtracker.domain.usecase.main_use_cases.food.ValidateAndSaveMealUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddMealViewModel @Inject constructor(
    private val validateAndSaveMealUseCase: ValidateAndSaveMealUseCase
) : BaseViewModel<AddMealUiState, AddMealIntent, AddMealEffect> (
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
                updateState { it.copy(foodName = intent.name) }
            }

            is AddMealIntent.OnQuantityChange ->
            {
                updateState { it.copy(quantity = intent.quantity) }
            }

            is AddMealIntent.OnUnitChange ->
            {
                updateState { it.copy(unit = intent.unit) }
            }

            is AddMealIntent.OnCaloriesChange ->
            {
                updateState { it.copy(calories = intent.calories) }
            }

            is AddMealIntent.OnMealTypeChange -> {
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
        if (foodName.isEmpty())
        {
            sendEffect(AddMealEffect.ShowError("Food name could not be blank"))
            return
        }

        val quantity = currentState.quantity.toFloatOrNull()
        if (quantity == null || quantity <= 0)
        {
            sendEffect(AddMealEffect.ShowError("Quantity could not be blank"))
            return
        }

        val calories = currentState.calories.toIntOrNull()
        if (calories == null || calories < 0)
        {
            sendEffect(AddMealEffect.ShowError("Please fill in the valid calories"))
            return
        }

        val newFood = TempFoodItem(
            foodName = foodName,
            quantity = quantity,
            unit = currentState.unit,
            calories = calories
        )

        val newAddedFoods = currentState.addedFoods + newFood

        updateState {
            it.copy(
                addedFoods = newAddedFoods,
                totalCalories = newAddedFoods.sumOf { item -> item.calories },
                foodName = "",
                quantity = "",
                calories = ""
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

        updateState { it.copy(isLoading = true, errorMessage = null) }

        launchSafe(
            onError = {throwable ->
                updateState { it.copy(isLoading = false, errorMessage = throwable.message) }
                sendEffect(AddMealEffect.ShowError(throwable.message ?: "Error saving meal"))
            }
        ) {
            validateAndSaveMealUseCase(
                currentFoods = currentFoods,
                dateText = currentState.dateText,
                mealType = currentState.mealType
            )

            updateState {
                it.copy(isLoading = false, addedFoods = emptyList(), totalCalories = 0)
            }

            sendEffect(AddMealEffect.NavigateBackWithSuccess)
        }
    }
}