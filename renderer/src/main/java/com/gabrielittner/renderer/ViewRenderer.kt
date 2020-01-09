package com.gabrielittner.renderer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        fun create(rootView: View): ViewRenderer<State, Action>
    }

    abstract class InflaterFactory<State, Action>(
        val layoutId: Int
    ) : Factory<State, Action> {

        fun inflate(parent: ViewGroup): ViewRenderer<State, Action> {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(layoutId, parent, false)
            return create(view)
        }
    }
}
