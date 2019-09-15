package com.gabrielittner.binder

import io.reactivex.Observable

interface Binder<State, Action> {
    val actions: Observable<Action>
    fun render(state: State)
}
