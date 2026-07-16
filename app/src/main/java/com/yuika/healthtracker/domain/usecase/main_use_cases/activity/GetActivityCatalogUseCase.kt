package com.yuika.healthtracker.domain.usecase.main_use_cases.activity

import com.yuika.healthtracker.domain.model.ActivityCatalog
import com.yuika.healthtracker.domain.repository.ActivityCatalogRepository
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetActivityCatalogUseCase @Inject constructor(
    private val getLatestUserUseCase: GetLatestUserUseCase,
    private val activityCatalogRepository: ActivityCatalogRepository
)
{
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<ActivityCatalog>> =
        getLatestUserUseCase().flatMapLatest { user ->
            if (user == null) flowOf(emptyList())
            else activityCatalogRepository.getAllByUser(user.id)
        }
}
