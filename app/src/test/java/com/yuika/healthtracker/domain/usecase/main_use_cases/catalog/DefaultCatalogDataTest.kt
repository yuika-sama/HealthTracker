package com.yuika.healthtracker.domain.usecase.main_use_cases.catalog

import com.yuika.healthtracker.domain.model.supportedFoodUnits
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultCatalogDataTest {
    @Test
    fun foodCatalogUsesSupportedUnits() {
        assertTrue(DefaultCatalogData.foods(userId = 1).all { it.unit in supportedFoodUnits })
    }
}
