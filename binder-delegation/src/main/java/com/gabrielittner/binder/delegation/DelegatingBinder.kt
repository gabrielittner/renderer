package com.gabrielittner.binder.delegation

import android.view.View
import com.gabrielittner.binder.Binder
import com.gabrielittner.binder.ViewBinder
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

abstract class DelegatingBinder<State, Action>(
    rootView: View
) : ViewBinder<State, Action>(rootView) {

    private val binders = mutableListOf<Binder<State, Action>>()

    private val actionSubject = PublishSubject.create<Action>()
    private val disposables = CompositeDisposable()

    final override val actions: Observable<Action> = actionSubject.hide()

    final override fun renderToView(state: State) {
        binders.forEach {
            it.render(state)
        }
    }

    protected fun <InnerState : State> addBinder(
        factory: Factory<InnerState, Action>,
        transformer: (State) -> InnerState
    ) {
        val binder = factory.create(rootView)
        val delegate: Binder<State, Action> =
            BinderDelegate(binder, transformer)
        val disposable = delegate.actions.subscribe(actionSubject::onNext)
        disposables.add(disposable)
        binders.add(delegate)
    }
}
