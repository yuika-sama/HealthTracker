package com.yuika.healthtracker.ui.core.base

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State: UiState, Intent: UiIntent, Effect: UiEffect>(
    initialState: State
) : ViewModel(){
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<Effect>(
        replay =  0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect: SharedFlow<Effect> = _effect.asSharedFlow()

    abstract fun onIntent(intent: Intent)

    protected fun updateState(transform: (State) -> State){
        _state.update(transform)
    }

    protected fun sendEffect(effect: Effect){
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    protected fun launchSafe(
        onError: ((Throwable) -> Unit)? = null,
        block: suspend () -> Unit
    ) {
        val exceptionHandler = CoroutineExceptionHandler{_, throwable ->
            onError?.invoke(throwable) ?: handleGlobalError(throwable)
        }
        viewModelScope.launch(exceptionHandler){
            block()
        }
    }

    protected fun handleGlobalError(throwable: Throwable)
    {
        throwable.printStackTrace()
        // TODO: sendEffect: toast display error
    }
}