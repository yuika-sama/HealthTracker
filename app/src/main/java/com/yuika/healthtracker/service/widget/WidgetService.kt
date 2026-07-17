package com.yuika.healthtracker.service.widget

import android.content.Context
import androidx.glance.appwidget.updateAll
import com.yuika.healthtracker.domain.usecase.main_use_cases.dashboard.GetDashboardDataUseCase
import com.yuika.healthtracker.ui.features.widget.AppWidgetInfo
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WidgetService @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val getDashboardDataUseCase: GetDashboardDataUseCase
)
{
    suspend fun loadTodayCalories(): WidgetCaloriesState
    {
        val data = getDashboardDataUseCase(LocalDate.now().toString()).firstOrNull()
            ?: return WidgetCaloriesState.empty()

        return WidgetCaloriesState(
            hasUser = true,
            targetCalories = data.goalCalories,
            eatenCalories = data.intakeCalories,
            burnedCalories = data.burnedCalories,
            balanceCalories = data.netBalance,
            remainingCalories = data.remainingCalories
        )
    }

    suspend fun refresh()
    {
        AppWidgetInfo().updateAll(context)
    }

    companion object
    {
        fun from(context: Context): WidgetService
        {
            return EntryPointAccessors.fromApplication(
                context.applicationContext,
                WidgetEntryPoint::class.java
            ).widgetService()
        }
    }
}