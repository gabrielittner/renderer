package com.gabrielittner.renderer.connect

import com.freeletics.mad.statemachine.StateMachine
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged

abstract class SimpleStateMachine<State : Any, Action : Any> : StateMachine<State, Action> {

    private val internalState = MutableSharedFlow<State>(replay = 1, onBufferOverflow = DROP_OLDEST)

    override val state: Flow<State> = internalState.distinctUntilChanged()

    private val internalActions = MutableSharedFlow<Action>()

    protected val actions: Flow<Action> = internalActions

    protected suspend fun updateState(state: State) {
        internalState.emit(state)
    }

    final override suspend fun dispatch(action: Action) {
        internalActions.emit(action)
    }
}
