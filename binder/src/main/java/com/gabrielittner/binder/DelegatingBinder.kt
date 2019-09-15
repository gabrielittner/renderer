package com.gabrielittner.binder

import android.view.View
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

internal abstract class DelegatingBinder<State, Action>(
    rootView: View
) : ViewBinder<State, Action>(rootView) {

    private val binders = mutableListOf<Binder<State, Action>>()

    private val actionSubject = PublishSubject.create<Action>()
    private val disposables = CompositeDisposable()

    override val actions: Observable<Action> = actionSubject.hide()

    override fun renderToView(state: State) {
        binders.forEach {
            it.render(state)
        }
    }

    protected inline fun <reified InnerState : State> addBinder(
        factory: Factory<InnerState, Action>,
        noinline transformer: (State) -> InnerState
    ) {
        val binder = factory.create(rootView)
        val delegate: Binder<State, Action> =
            BinderDelegate(binder, transformer)
        val disposable = delegate.actions.subscribe(actionSubject::onNext)
        disposables.add(disposable)
        binders.add(delegate)
    }
}

internal class BinderDelegate<State, InnerState, Action>(
    val binder: Binder<InnerState, Action>,
    val transformer: (State) -> InnerState
): Binder<State, Action> {
    override val actions: Observable<Action>
        get() = binder.actions

    override fun render(state: State) = binder.render(transformer(state))
}
