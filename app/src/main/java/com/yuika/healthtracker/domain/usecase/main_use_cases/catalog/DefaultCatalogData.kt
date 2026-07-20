package com.yuika.healthtracker.domain.usecase.main_use_cases.catalog

import com.yuika.healthtracker.domain.model.ActivityCatalog
import com.yuika.healthtracker.domain.model.FoodCatalog

object DefaultCatalogData
{
    fun foods(userId: Int) = listOf(
        FoodCatalog(userId = userId, name = "White rice", caloriesPerServing = 130, defaultQuantity = 100f, unit = "g"),
        FoodCatalog(userId = userId, name = "Boiled egg", caloriesPerServing = 78, defaultQuantity = 1f, unit = "piece"),
        FoodCatalog(userId = userId, name = "Beef pho", caloriesPerServing = 350, defaultQuantity = 1f, unit = "bowl"),
        FoodCatalog(userId = userId, name = "Banh mi", caloriesPerServing = 265, defaultQuantity = 1f, unit = "piece"),
        FoodCatalog(userId = userId, name = "Chicken breast", caloriesPerServing = 165, defaultQuantity = 100f, unit = "g"),
        FoodCatalog(userId = userId, name = "Salmon", caloriesPerServing = 208, defaultQuantity = 100f, unit = "g"),
        FoodCatalog(userId = userId, name = "Pork", caloriesPerServing = 242, defaultQuantity = 100f, unit = "g"),
        FoodCatalog(userId = userId, name = "Tofu", caloriesPerServing = 76, defaultQuantity = 100f, unit = "g"),
        FoodCatalog(userId = userId, name = "Sweet potato", caloriesPerServing = 86, defaultQuantity = 100f, unit = "g"),
        FoodCatalog(userId = userId, name = "Banana", caloriesPerServing = 105, defaultQuantity = 1f, unit = "piece"),
        FoodCatalog(userId = userId, name = "Apple", caloriesPerServing = 95, defaultQuantity = 1f, unit = "piece"),
        FoodCatalog(userId = userId, name = "Avocado", caloriesPerServing = 160, defaultQuantity = 100f, unit = "g"),
        FoodCatalog(userId = userId, name = "Milk", caloriesPerServing = 122, defaultQuantity = 250f, unit = "ml"),
        FoodCatalog(userId = userId, name = "Yogurt", caloriesPerServing = 100, defaultQuantity = 1f, unit = "serving"),
        FoodCatalog(userId = userId, name = "Oatmeal", caloriesPerServing = 150, defaultQuantity = 40f, unit = "g"),
        FoodCatalog(userId = userId, name = "Bread", caloriesPerServing = 80, defaultQuantity = 1f, unit = "piece"),
        FoodCatalog(userId = userId, name = "Cheese", caloriesPerServing = 113, defaultQuantity = 1f, unit = "piece"),
        FoodCatalog(userId = userId, name = "Peanut butter", caloriesPerServing = 188, defaultQuantity = 1f, unit = "serving"),
        FoodCatalog(userId = userId, name = "Fried rice", caloriesPerServing = 330, defaultQuantity = 1f, unit = "plate"),
        FoodCatalog(userId = userId, name = "Noodle soup", caloriesPerServing = 400, defaultQuantity = 1f, unit = "bowl"),
        FoodCatalog(userId = userId, name = "Spring roll", caloriesPerServing = 65, defaultQuantity = 1f, unit = "piece"),
        FoodCatalog(userId = userId, name = "Dumpling", caloriesPerServing = 45, defaultQuantity = 1f, unit = "piece"),
        FoodCatalog(userId = userId, name = "Green salad", caloriesPerServing = 80, defaultQuantity = 1f, unit = "serving"),
        FoodCatalog(userId = userId, name = "Tomato", caloriesPerServing = 18, defaultQuantity = 100f, unit = "g"),
        FoodCatalog(userId = userId, name = "Cucumber", caloriesPerServing = 15, defaultQuantity = 100f, unit = "g"),
        FoodCatalog(userId = userId, name = "Broccoli", caloriesPerServing = 55, defaultQuantity = 100f, unit = "g"),
        FoodCatalog(userId = userId, name = "Orange juice", caloriesPerServing = 112, defaultQuantity = 250f, unit = "ml"),
        FoodCatalog(userId = userId, name = "Black coffee", caloriesPerServing = 2, defaultQuantity = 250f, unit = "ml"),
        FoodCatalog(userId = userId, name = "Bubble tea", caloriesPerServing = 450, defaultQuantity = 500f, unit = "ml"),
        FoodCatalog(userId = userId, name = "Pizza slice", caloriesPerServing = 285, defaultQuantity = 1f, unit = "piece")
    )

    fun activities(userId: Int) = listOf(
        ActivityCatalog(userId = userId, name = "Walking", met = 3.5, iconName = "walk", intensity = "Light"),
        ActivityCatalog(userId = userId, name = "Running", met = 9.8, iconName = "run", intensity = "Strong"),
        ActivityCatalog(userId = userId, name = "Cycling", met = 7.5, iconName = "bike", intensity = "Strong"),
        ActivityCatalog(userId = userId, name = "Swimming", met = 8.0, iconName = "swim", intensity = "Strong"),
        ActivityCatalog(userId = userId, name = "Yoga", met = 3.0, iconName = "yoga", intensity = "Light"),
        ActivityCatalog(userId = userId, name = "Gym", met = 6.0, iconName = "gym", intensity = "Medium"),
        ActivityCatalog(userId = userId, name = "Stair climbing", met = 8.8, iconName = "stairs", intensity = "Strong"),
        ActivityCatalog(userId = userId, name = "Jump rope", met = 12.3, iconName = "jump_rope", intensity = "Strong"),
        ActivityCatalog(userId = userId, name = "Badminton", met = 5.5, iconName = "badminton", intensity = "Medium"),
        ActivityCatalog(userId = userId, name = "Football", met = 7.0, iconName = "football", intensity = "Strong"),
        ActivityCatalog(userId = userId, name = "Basketball", met = 6.5, iconName = "basketball", intensity = "Medium"),
        ActivityCatalog(userId = userId, name = "Dancing", met = 5.0, iconName = "dance", intensity = "Medium"),
        ActivityCatalog(userId = userId, name = "Hiking", met = 6.0, iconName = "hiking", intensity = "Medium"),
        ActivityCatalog(userId = userId, name = "Table tennis", met = 4.0, iconName = "table_tennis", intensity = "Light"),
        ActivityCatalog(userId = userId, name = "Housework", met = 3.3, iconName = "home", intensity = "Light")
    )
}
