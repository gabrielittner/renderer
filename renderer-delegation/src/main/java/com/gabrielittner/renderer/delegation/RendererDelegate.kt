package com.gabrielittner.renderer.delegation

import com.gabrielittner.renderer.Renderer
import io.reactivex.Observable

internal class RendererDelegate<State, InnerState, Action>(
    private val renderer: Renderer<InnerState, Action>,
    private val transformer: (State) -> InnerState
): Renderer<State, Action> {
    override val actions: Observable<Action>
        get() = renderer.actions

    override fun render(state: State) = renderer.render(transformer(state))
}
