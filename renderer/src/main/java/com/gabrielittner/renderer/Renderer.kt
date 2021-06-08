package com.gabrielittner.renderer

import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow

/**
 * Represents a piece of UI that can [render] given [State] objects and emits [Action] objects
 * based on user [actions].
 */
interface Renderer<State : Any, Action : Any> {
    /**
     * Provides an [Observable] of user actions in the form of [Action] objects.
     */
    val actions: Flow<Action>

    /**
     * Render the given [State] to the UI.
     */
    fun render(state: State)
}
