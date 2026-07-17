package com.yuika.healthtracker.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppStartViewModel @Inject constructor(
    private val getLatestUserUseCase: GetLatestUserUseCase
) : ViewModel()
{
    private val _startRoute = MutableStateFlow<Route?>(null)
    val startRoute: StateFlow<Route?> = _startRoute

    init {
        viewModelScope.launch {
            _startRoute.value = if (getLatestUserUseCase().firstOrNull() == null) {
                Route.Onboarding1
            } else {
                Route.Dashboard
            }
        }
    }
}
