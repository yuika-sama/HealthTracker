package com.yuika.healthtracker.domain.usecase.main_use_cases.food

import com.yuika.healthtracker.domain.model.FoodCatalog
import com.yuika.healthtracker.domain.repository.FoodCatalogRepository
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class SearchFoodCatalogUseCase @Inject constructor(
    private val getLatestUserUseCase: GetLatestUserUseCase,
    private val foodCatalogRepository: FoodCatalogRepository
)
{
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(query: String) : Flow<List<FoodCatalog>> =
        getLatestUserUseCase().flatMapLatest { user ->
            if (user == null || query.isBlank()) flowOf(emptyList())
            else foodCatalogRepository.searchByUser(user.id, query.trim())
        }
}
