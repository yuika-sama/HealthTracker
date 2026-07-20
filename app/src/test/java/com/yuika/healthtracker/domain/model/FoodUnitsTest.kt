package com.yuika.healthtracker.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class FoodUnitsTest {
    @Test
    fun canonicalFoodUnitNormalizesLegacyUnits() {
        assertEquals("bowl", canonicalFoodUnit("Bowl (Smol)"))
        assertEquals("bowl", canonicalFoodUnit("Bowl (Small)"))
        assertEquals("plate", canonicalFoodUnit("Plate (Med)"))
        assertEquals("g", canonicalFoodUnit("Gram"))
        assertEquals("piece", canonicalFoodUnit("slice"))
        assertEquals("serving", canonicalFoodUnit("cup"))
        assertEquals("serving", canonicalFoodUnit("tbsp"))
    }
}
