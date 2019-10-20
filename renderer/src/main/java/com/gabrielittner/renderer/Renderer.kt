package com.gabrielittner.renderer

import io.reactivex.Observable

interface Renderer<State, Action> {
    val actions: Observable<Action>
    fun render(state: State)
}
