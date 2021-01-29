package com.gabrielittner.renderer.delegation

import android.view.View
import com.gabrielittner.renderer.Renderer
import com.gabrielittner.renderer.ViewRenderer
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

abstract class DelegatingRenderer<State : Any, Action : Any>(
    rootView: View
) : ViewRenderer<State, Action>(rootView) {

    private val renderers = mutableListOf<Renderer<State, Action>>()

    private val disposables = CompositeDisposable()

    final override fun renderToView(state: State) {
        renderers.forEach {
            it.render(state)
        }
    }

    protected fun addRenderer(
        factory: Factory<* , out ViewRenderer<State, Action>>
    ) {
        addRenderer<State, Action>(factory, { it }, { it })
    }

    protected fun <InnerState : Any> addRenderer(
        factory: Factory<* , out ViewRenderer<InnerState, Action>>,
        stateTransformer: (State) -> InnerState
    ) {
        addRenderer<InnerState, Action>(factory, stateTransformer, { it })
    }

    protected fun <InnerState : Any, InnerAction : Any> addRenderer(
        factory: Factory<* , out ViewRenderer<InnerState, Action>>,
        stateTransformer: (State) -> InnerState,
        actionTransformer: (InnerAction) -> Action
    ) {
        val renderer = factory.create(rootView)
        val delegate: Renderer<State, Action> =
            RendererDelegate(renderer, stateTransformer, actionTransformer)
        val disposable = delegate.actions.subscribe(this::sendAction)
        disposables.add(disposable)
        renderers.add(delegate)
    }
}
