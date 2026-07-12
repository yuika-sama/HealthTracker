package com.yuika.healthtracker.ui.features.main_features.add_meal

import com.yuika.healthtracker.data.local.entity.FoodEntryEntity
import com.yuika.healthtracker.domain.repository.FoodEntryRepository
import com.yuika.healthtracker.domain.repository.UserRepository
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMealViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val foodEntryRepository: FoodEntryRepository
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
            sendEffect(AddMealEffect.ShowError("Vui lòng nhập lượng calo hợp lệ"))
            return
        }

        val newFood = TempFoodItem(
            foodName = foodName,
            quantity = quantity,
            unit = currentState.unit,
            calories = calories
        )

        val newAddedFoods = currentState.addedFoods + newFood

        updateState { it ->
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

        if (currentFoods.isEmpty()){
            sendEffect(AddMealEffect.ShowError("Please at least add one food in the meal"))
            return
        }
        updateState { it.copy(isLoading = true, errorMessage = null) }

        launchSafe(
            onError = {throwable ->
                updateState { it.copy(isLoading = true, errorMessage = null) }
            }
        ) {
            val user = userRepository.getLatestUserFlow().firstOrNull()

            if (user == null){
                updateState {
                    it.copy(isLoading = false, errorMessage = "Can't find user information")
                }
                sendEffect(AddMealEffect.ShowError("Can't find user information"))
                return@launchSafe
            }

            currentFoods.forEach { tempFood ->
                val entity = FoodEntryEntity(
                    userId = user.id,
                    dateText = currentState.dateText,
                    mealType = currentState.mealType,
                    foodName = tempFood.foodName,
                    quantity = tempFood.quantity,
                    unit = tempFood.unit,
                    calories = tempFood.calories,
                    imagePath = null
                )
                foodEntryRepository.insertFoodEntry(entity)
            }

            updateState {
                it.copy(isLoading = false, addedFoods = emptyList(), totalCalories = 0)
            }

            sendEffect(AddMealEffect.NavigateBackWithSuccess)
        }
    }
}