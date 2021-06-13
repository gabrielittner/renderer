package com.gabrielittner.renderer.connect

import com.freeletics.mad.statemachine.StateMachine
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged

abstract class RxSimpleStateMachine<State : Any, Action : Any> : StateMachine<State, Action> {

    private val stateFlow = MutableSharedFlow<State>(replay = 1, onBufferOverflow = DROP_OLDEST)

    override val state: Flow<State> = stateFlow.distinctUntilChanged()

    private val actionSubject = PublishSubject.create<Action>()

    protected val actions: Observable<Action> = actionSubject.hide()

    protected fun updateState(state: State) {
        stateFlow.tryEmit(state)
    }

    final override suspend fun dispatch(action: Action) {
        actionSubject.onNext(action)
    }
}
