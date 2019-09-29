package com.gabrielittner.binder.connectors

import io.reactivex.Observable

interface RxViewModel<State, Action> {
    fun observe(events: Observable<Action>): Observable<State>
}
