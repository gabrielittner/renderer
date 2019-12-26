package com.gabrielittner.renderer

import android.view.View
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

abstract class ViewRenderer<State, Action>(
    val rootView: View
) : Renderer<State, Action> {

    private val internalActions: PublishSubject<Action> = PublishSubject.create()

    private var internalState: State? = null
    protected val state: State? get() = internalState

    final override val actions: Observable<Action>
        get() = Observable.merge(internalActions, viewActions)

    protected fun sendAction(action: Action) {
        internalActions.onNext(action)
    }

    protected open val viewActions: Observable<Action> = Observable.never()

    final override fun render(state: State) {
        internalState = state
        renderToView(state)
    }

    protected abstract fun renderToView(state: State)


    interface Factory<State, Action> {
        fun create(rootView: View): Renderer<State, Action>
    }
}
