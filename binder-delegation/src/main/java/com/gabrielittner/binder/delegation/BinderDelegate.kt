package com.gabrielittner.binder.delegation

import com.gabrielittner.binder.Binder
import io.reactivex.Observable

internal class BinderDelegate<State, InnerState, Action>(
    private val binder: Binder<InnerState, Action>,
    private val transformer: (State) -> InnerState
): Binder<State, Action> {
    override val actions: Observable<Action>
        get() = binder.actions

    override fun render(state: State) = binder.render(transformer(state))
}
