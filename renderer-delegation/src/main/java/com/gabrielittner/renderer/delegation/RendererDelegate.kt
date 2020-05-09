package com.gabrielittner.renderer.delegation

import com.gabrielittner.renderer.Renderer
import io.reactivex.Observable

internal class RendererDelegate<State, InnerState, Action, InnerAction>(
    private val renderer: Renderer<InnerState, InnerAction>,
    private val stateTransformer: (State) -> InnerState,
    private val actionTransformer: (InnerAction) -> Action
): Renderer<State, Action> {
    override val actions: Observable<Action>
        get() = renderer.actions.map { actionTransformer(it) }

    override fun render(state: State) = renderer.render(stateTransformer(state))
}
