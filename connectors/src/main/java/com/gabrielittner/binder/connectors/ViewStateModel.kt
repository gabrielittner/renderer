package com.gabrielittner.binder.connectors

import io.reactivex.Observable

interface ViewStateModel<State, Action> {
    fun observe(events: Observable<Action>): Observable<State>
}
