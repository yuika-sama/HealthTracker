package com.yuika.healthtracker.domain.usecase.main_use_cases.catalog

import com.yuika.healthtracker.domain.repository.ActivityCatalogRepository
import com.yuika.healthtracker.domain.repository.FoodCatalogRepository
import javax.inject.Inject

class EnsureUserCatalogSeedUseCase @Inject constructor(
    private val foodCatalogRepository: FoodCatalogRepository,
    private val activityCatalogRepository: ActivityCatalogRepository
)
{
    suspend operator fun invoke(userId: Int)
    {
        if (foodCatalogRepository.isEmptyForUser(userId)) {
            foodCatalogRepository.insertAll(DefaultCatalogData.foods(userId))
        }

        if (activityCatalogRepository.isEmptyForUser(userId)) {
            activityCatalogRepository.insertAll(DefaultCatalogData.activities(userId))
        }
    }
}
