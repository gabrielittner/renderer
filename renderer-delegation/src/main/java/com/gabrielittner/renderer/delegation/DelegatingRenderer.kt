package com.gabrielittner.renderer.delegation

import android.view.View
import com.gabrielittner.renderer.Renderer
import com.gabrielittner.renderer.ViewRenderer
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

abstract class DelegatingRenderer<State, Action>(
    rootView: View
) : ViewRenderer<State, Action>(rootView) {

    private val renderers = mutableListOf<Renderer<State, Action>>()

    private val disposables = CompositeDisposable()

    final override fun renderToView(state: State) {
        renderers.forEach {
            it.render(state)
        }
    }

    protected fun <InnerState : State> addRenderer(
        factory: Factory<InnerState, Action>,
        transformer: (State) -> InnerState
    ) {
        val renderer = factory.create(rootView)
        val delegate: Renderer<State, Action> =
            RendererDelegate(renderer, transformer)
        val disposable = delegate.actions.subscribe(this::sendAction)
        disposables.add(disposable)
        renderers.add(delegate)
    }
}
