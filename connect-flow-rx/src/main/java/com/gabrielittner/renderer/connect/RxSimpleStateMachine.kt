package com.gabrielittner.renderer.connect

import com.freeletics.mad.statemachine.StateMachine
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class RxSimpleStateMachine<State : Any, Action : Any>(
    initialState: State
) : StateMachine<State, Action> {

    private val internalState = MutableStateFlow(initialState)

    override val state: StateFlow<State> = internalState

    private val actionSubject = PublishSubject.create<Action>()

    protected val actions: Observable<Action> = actionSubject.hide()

    protected fun updateState(state: State) {
        check(internalState.tryEmit(state))
    }

    final override suspend fun dispatch(action: Action) {
        actionSubject.onNext(action)
    }
}
