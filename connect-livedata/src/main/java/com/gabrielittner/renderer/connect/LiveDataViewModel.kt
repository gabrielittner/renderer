package com.gabrielittner.renderer.connect

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.gabrielittner.renderer.Renderer

interface LiveDataViewModel<State, Action> {
    fun handleAction(action: Action)
    fun observe(): LiveData<State>
}

fun <State, Action> LifecycleOwner.connect(
    renderer: Renderer<State, Action>,
    model: LiveDataViewModel<State, Action>
) {
    lifecycle.addObserver(RendererActionObserver(renderer, model::handleAction))
    model.observe().observe(this, Observer { renderer.render(it) })
}
