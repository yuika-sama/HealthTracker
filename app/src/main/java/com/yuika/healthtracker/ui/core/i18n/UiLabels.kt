package com.yuika.healthtracker.ui.core.i18n

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.yuika.healthtracker.R
import com.yuika.healthtracker.domain.model.AppFontSize
import com.yuika.healthtracker.domain.model.AppLanguage
import com.yuika.healthtracker.domain.model.ThemeColorPreset
import com.yuika.healthtracker.domain.model.ThemeMode
import com.yuika.healthtracker.domain.model.canonicalFoodUnit
import com.yuika.healthtracker.ui.core.model.IntensityLevel
import java.util.Locale

@Composable
fun currentLocale(): Locale = LocalConfiguration.current.locales[0]

@Composable
fun appLanguageLabel(value: AppLanguage): String = when (value) {
    AppLanguage.EN -> stringResource(R.string.language_en)
    AppLanguage.VI -> stringResource(R.string.language_vi)
}

@Composable
fun themeModeLabel(value: ThemeMode): String = when (value) {
    ThemeMode.LIGHT -> stringResource(R.string.theme_light)
    ThemeMode.DARK -> stringResource(R.string.theme_dark)
    ThemeMode.SYSTEM -> stringResource(R.string.theme_system)
}

@Composable
fun themeColorLabel(value: ThemeColorPreset): String = when (value) {
    ThemeColorPreset.GREEN -> stringResource(R.string.theme_color_green)
    ThemeColorPreset.BLUE -> stringResource(R.string.theme_color_blue)
    ThemeColorPreset.PURPLE -> stringResource(R.string.theme_color_purple)
    ThemeColorPreset.ORANGE -> stringResource(R.string.theme_color_orange)
}

@Composable
fun fontSizeLabel(value: AppFontSize): String = when (value) {
    AppFontSize.SMALL -> stringResource(R.string.font_size_small)
    AppFontSize.MEDIUM -> stringResource(R.string.font_size_medium)
    AppFontSize.LARGE -> stringResource(R.string.font_size_large)
}

@Composable
fun genderLabel(value: String): String = when (value.trim().lowercase(Locale.ROOT)) {
    "male", "nam" -> stringResource(R.string.gender_male)
    "female", "nu", "nữ" -> stringResource(R.string.gender_female)
    else -> value
}

@Composable
fun mealTypeLabel(value: String): String = when (value.trim().lowercase(Locale.ROOT)) {
    "breakfast" -> stringResource(R.string.meal_breakfast)
    "lunch" -> stringResource(R.string.meal_lunch)
    "dinner" -> stringResource(R.string.meal_dinner)
    "snack" -> stringResource(R.string.meal_snack)
    else -> value
}

@Composable
fun activityLevelTitle(value: String): String = when (value) {
    "sedentary" -> stringResource(R.string.activity_level_sedentary)
    "lightly_active" -> stringResource(R.string.activity_level_lightly_active)
    "moderately_active" -> stringResource(R.string.activity_level_moderately_active)
    "very_active" -> stringResource(R.string.activity_level_very_active)
    "extra_active" -> stringResource(R.string.activity_level_extra_active)
    else -> stringResource(R.string.activity_level_active)
}

@Composable
fun activityLevelDescription(value: String): String = when (value) {
    "sedentary" -> stringResource(R.string.activity_level_sedentary_desc)
    "lightly_active" -> stringResource(R.string.activity_level_lightly_active_desc)
    "moderately_active" -> stringResource(R.string.activity_level_moderately_active_desc)
    "very_active" -> stringResource(R.string.activity_level_very_active_desc)
    "extra_active" -> stringResource(R.string.activity_level_extra_active_desc)
    else -> ""
}

@Composable
fun activityMultiplierLabel(value: String): String = when (value) {
    "sedentary" -> "1.2x ${activityLevelTitle(value)}"
    "lightly_active" -> "1.375x ${activityLevelTitle(value)}"
    "moderately_active" -> "1.55x ${activityLevelTitle(value)}"
    "very_active" -> "1.725x ${activityLevelTitle(value)}"
    "extra_active" -> "1.9x ${activityLevelTitle(value)}"
    else -> "1.55x ${activityLevelTitle("moderately_active")}"
}

@Composable
fun goalTitle(value: String): String = when (value) {
    "lose_weight", "Lose weight", "Lose Weight" -> stringResource(R.string.goal_lose_weight)
    "maintain_weight", "Maintain weight", "Maintain Weight" -> stringResource(R.string.goal_maintain_weight)
    "gain_weight", "Weight gain", "Gain weight", "Gain Weight" -> stringResource(R.string.goal_gain_weight)
    else -> value
}

@Composable
fun goalDescription(value: String): String = when (value) {
    "lose_weight" -> stringResource(R.string.goal_lose_weight_desc)
    "maintain_weight" -> stringResource(R.string.goal_maintain_weight_desc)
    "gain_weight" -> stringResource(R.string.goal_gain_weight_desc)
    else -> ""
}

@Composable
fun intensityLabel(value: IntensityLevel): String = when (value) {
    IntensityLevel.LIGHT -> stringResource(R.string.intensity_light)
    IntensityLevel.MEDIUM -> stringResource(R.string.intensity_medium)
    IntensityLevel.STRONG -> stringResource(R.string.intensity_strong)
}

@Composable
fun intensityLabel(value: String): String = when (value.trim().lowercase(Locale.ROOT)) {
    "light" -> stringResource(R.string.intensity_light)
    "medium" -> stringResource(R.string.intensity_medium)
    "strong" -> stringResource(R.string.intensity_strong)
    else -> value
}

@Composable
fun bmiCategoryLabel(value: String): String = when (value.trim().lowercase(Locale.ROOT)) {
    "underweight" -> stringResource(R.string.bmi_underweight)
    "normal" -> stringResource(R.string.bmi_normal)
    "overweight" -> stringResource(R.string.bmi_overweight)
    "obese" -> stringResource(R.string.bmi_obese)
    "unknown" -> stringResource(R.string.bmi_unknown)
    else -> value
}

@Composable
fun unitLabel(value: String): String = when (canonicalFoodUnit(value)) {
    "plate" -> stringResource(R.string.unit_plate)
    "bowl" -> stringResource(R.string.unit_bowl)
    "serving" -> stringResource(R.string.unit_serving)
    "g" -> stringResource(R.string.unit_g)
    "kg" -> stringResource(R.string.unit_kg)
    "cm" -> stringResource(R.string.unit_cm)
    "ml" -> stringResource(R.string.unit_ml)
    "piece" -> stringResource(R.string.unit_piece)
    else -> value
}

@Composable
fun foodCatalogLabel(value: String): String = when (value.trim().lowercase(Locale.ROOT)) {
    "white rice" -> stringResource(R.string.food_white_rice)
    "boiled egg" -> stringResource(R.string.food_boiled_egg)
    "beef pho" -> stringResource(R.string.food_beef_pho)
    "banh mi" -> stringResource(R.string.food_banh_mi)
    "chicken breast" -> stringResource(R.string.food_chicken_breast)
    "salmon" -> stringResource(R.string.food_salmon)
    "pork" -> stringResource(R.string.food_pork)
    "tofu" -> stringResource(R.string.food_tofu)
    "sweet potato" -> stringResource(R.string.food_sweet_potato)
    "banana" -> stringResource(R.string.food_banana)
    "apple" -> stringResource(R.string.food_apple)
    "avocado" -> stringResource(R.string.food_avocado)
    "milk" -> stringResource(R.string.food_milk)
    "yogurt" -> stringResource(R.string.food_yogurt)
    "oatmeal" -> stringResource(R.string.food_oatmeal)
    "bread" -> stringResource(R.string.food_bread)
    "cheese" -> stringResource(R.string.food_cheese)
    "peanut butter" -> stringResource(R.string.food_peanut_butter)
    "fried rice" -> stringResource(R.string.food_fried_rice)
    "noodle soup" -> stringResource(R.string.food_noodle_soup)
    "spring roll" -> stringResource(R.string.food_spring_roll)
    "dumpling" -> stringResource(R.string.food_dumpling)
    "green salad" -> stringResource(R.string.food_green_salad)
    "tomato" -> stringResource(R.string.food_tomato)
    "cucumber" -> stringResource(R.string.food_cucumber)
    "broccoli" -> stringResource(R.string.food_broccoli)
    "orange juice" -> stringResource(R.string.food_orange_juice)
    "black coffee" -> stringResource(R.string.food_black_coffee)
    "bubble tea" -> stringResource(R.string.food_bubble_tea)
    "pizza slice" -> stringResource(R.string.food_pizza_slice)
    else -> value
}

@Composable
fun activityCatalogLabel(name: String): String = when (name.trim().lowercase(Locale.ROOT)) {
    "walking" -> stringResource(R.string.catalog_activity_walking)
    "running" -> stringResource(R.string.catalog_activity_running)
    "cycling" -> stringResource(R.string.catalog_activity_cycling)
    "swimming" -> stringResource(R.string.catalog_activity_swimming)
    "yoga" -> stringResource(R.string.catalog_activity_yoga)
    "gym" -> stringResource(R.string.catalog_activity_gym)
    "stair climbing" -> stringResource(R.string.catalog_activity_stair_climbing)
    "jump rope" -> stringResource(R.string.catalog_activity_jump_rope)
    "badminton" -> stringResource(R.string.catalog_activity_badminton)
    "football" -> stringResource(R.string.catalog_activity_football)
    "basketball" -> stringResource(R.string.catalog_activity_basketball)
    "dancing" -> stringResource(R.string.catalog_activity_dancing)
    "hiking" -> stringResource(R.string.catalog_activity_hiking)
    "table tennis" -> stringResource(R.string.catalog_activity_table_tennis)
    "housework" -> stringResource(R.string.catalog_activity_housework)
    else -> name
}

@Composable
fun localizedMessage(value: String): String = when (value) {
    "Activity level is not valid" -> stringResource(R.string.error_activity_level_invalid)
    "Can't export report" -> stringResource(R.string.error_export_report)
    "Can't find user data" -> stringResource(R.string.error_cannot_find_user_data)
    "Can't find user information", "Can't find user information." -> stringResource(R.string.error_cannot_find_user_info)
    "Can't get activity data" -> stringResource(R.string.error_cannot_get_activity_data)
    "Can't get data" -> stringResource(R.string.error_cannot_get_data)
    "Can't get diary data" -> stringResource(R.string.error_cannot_get_diary_data)
    "Can't load activity catalog" -> stringResource(R.string.error_cannot_load_activity_catalog)
    "Can't save avatar" -> stringResource(R.string.error_cannot_save_avatar)
    "Can't save notification settings" -> stringResource(R.string.error_cannot_save_notification_settings)
    "Can't save settings" -> stringResource(R.string.error_cannot_save_settings)
    "Can't save test notification settings" -> stringResource(R.string.error_cannot_save_test_notification_settings)
    "Can't write a log a day in future", "Cannot write a log for a future day" -> stringResource(R.string.error_future_log_date)
    "Date of birth is invalid" -> stringResource(R.string.error_date_of_birth_invalid)
    "Error calculating target" -> stringResource(R.string.error_calculate_target)
    "Error loading information", "Error loading profile" -> stringResource(R.string.error_profile_loading)
    "Error saving information" -> stringResource(R.string.error_save_information)
    "Error saving meal" -> stringResource(R.string.error_save_meal)
    "Error saving profile" -> stringResource(R.string.error_profile_saving)
    "Food name could not be blank" -> stringResource(R.string.error_food_name_blank)
    "Height must be a number" -> stringResource(R.string.error_height_number)
    "Height must be between 80 and 250 cm" -> stringResource(R.string.error_height_between)
    "Invalid date" -> stringResource(R.string.error_invalid_date)
    "Invalid meal type" -> stringResource(R.string.error_invalid_meal_type)
    "Meal type is not valid" -> stringResource(R.string.error_invalid_meal_type)
    "Please add at least one food to the meal", "Please at least add one food in the meal" -> stringResource(R.string.error_need_one_food)
    "Please enter a valid date of birth (yyyy-MM-dd, 10-120 years old)" -> stringResource(R.string.error_enter_valid_dob)
    "Please enter your full name", "Please fill in your name" -> stringResource(R.string.error_enter_full_name)
    "Please enter your height" -> stringResource(R.string.error_enter_height)
    "Please enter your weight" -> stringResource(R.string.error_enter_weight)
    "Please fill in practice duration", "Please fill in valid practice duration" -> stringResource(R.string.error_valid_duration)
    "Please fill in the valid calories", "Please fill in valid calories" -> stringResource(R.string.error_valid_calories)
    "Please let me know your infomation." -> stringResource(R.string.error_save_information)
    "Please select an activity" -> stringResource(R.string.error_select_activity)
    "Please select avatar" -> stringResource(R.string.error_select_avatar)
    "Please select food unit" -> stringResource(R.string.error_select_food_unit)
    "Please select your activity level" -> stringResource(R.string.error_select_activity_level)
    "Please select your date of birth" -> stringResource(R.string.error_select_date_of_birth)
    "Please select your gender" -> stringResource(R.string.error_select_gender)
    "Please select your goal" -> stringResource(R.string.error_select_goal)
    "Quantity could not be blank" -> stringResource(R.string.error_quantity_blank)
    "Quantity must be greater than 0" -> stringResource(R.string.error_quantity_positive)
    "Unknown error" -> stringResource(R.string.error_unknown)
    "Unknown user" -> stringResource(R.string.error_unknown_user)
    "Update successfully" -> stringResource(R.string.update_profile_success)
    "User not found" -> stringResource(R.string.error_cannot_find_user_info)
    "Weight and height must be a valid number" -> stringResource(R.string.error_weight_height_number)
    "Weight must be a number" -> stringResource(R.string.error_weight_number)
    "Weight must be between 20 and 300 kg" -> stringResource(R.string.error_weight_between)
    else -> value
}
