package com.yuika.healthtracker.domain.model

import java.util.Locale

val supportedFoodUnits = listOf("g", "ml", "piece", "bowl", "plate", "serving")

fun canonicalFoodUnit(value: String): String = when (value.trim().lowercase(Locale.ROOT)) {
    "plate (med)" -> "plate"
    "bowl (smol)", "bowl (small)" -> "bowl"
    "gram", "grams" -> "g"
    "cup", "tbsp" -> "serving"
    "slice" -> "piece"
    in supportedFoodUnits -> value.trim().lowercase(Locale.ROOT)
    else -> value
}
