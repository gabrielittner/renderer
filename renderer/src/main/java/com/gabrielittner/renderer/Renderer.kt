package com.gabrielittner.renderer

import io.reactivex.Observable

/**
 * Represents a piece of UI that can [render] given [State] objects and emits [Action] objects
 * based on user [actions].
 */
interface Renderer<State, Action> {
    /**
     * Provides an [Observable] of user actions in the form of [Action] objects.
     */
    val actions: Observable<Action>

    /**
     * Render the given [State] to the UI.
     */
    fun render(state: State)
}
