package com.gabrielittner.renderer.connect

import com.freeletics.mad.statemachine.StateMachine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class SimpleStateMachine<State : Any, Action : Any>(
    initialState: State
) : StateMachine<State, Action> {

    private val internalState = MutableStateFlow(initialState)

    override val state: StateFlow<State> = internalState

    private val internalActions = MutableSharedFlow<Action>()

    protected val actions: Flow<Action> = internalActions

    protected suspend fun updateState(state: State) {
        internalState.emit(state)
    }

    final override suspend fun dispatch(action: Action) {
        internalActions.emit(action)
    }
}
