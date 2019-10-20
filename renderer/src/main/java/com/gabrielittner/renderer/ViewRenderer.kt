package com.gabrielittner.renderer

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import io.reactivex.Observable

abstract class ViewRenderer<State, Action>(
    val rootView: View
) : Renderer<State, Action> {

    private var internalState: State? = null
    protected val state: State? get() = internalState

    abstract override val actions: Observable<Action>

    final override fun render(state: State) {
        internalState = state
        renderToView(state)
    }

    protected abstract fun renderToView(state: State)


    interface Factory<State, Action> {
        fun create(rootView: View): Renderer<State, Action>
    }
}
