package com.gabrielittner.renderer.connect

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.gabrielittner.renderer.Renderer
import io.reactivex.Observable

interface RxViewModel<State, Action> {
    fun observe(events: Observable<Action>): Observable<State>
}

fun <State, Action> Fragment.connect(
    renderer: Renderer<State, Action>,
    model: RxViewModel<State, Action>
) {
    viewLifecycleOwner.connect(renderer, model)
}

fun <State, Action> LifecycleOwner.connect(
    renderer: Renderer<State, Action>,
    model: RxViewModel<State, Action>
) {
    lifecycle.addObserver(RxWhileStartedObserver(renderer, model))
}
