package com.gabrielittner.renderer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

abstract class ViewRenderer<State, Action>(
    val rootView: View
) : Renderer<State, Action> {

    private val internalActions: PublishSubject<Action> = PublishSubject.create()

    private var internalState: State? = null
    protected val state: State get() = internalState!!

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
        fun create(rootView: View): ViewRenderer<State, Action>
    }

    interface InflaterFactory<State, Action> {
        fun inflate(parent: ViewGroup): ViewRenderer<State, Action>
    }

    abstract class LayoutInflaterFactory<State, Action>(
        private val layoutId: Int
    ) : InflaterFactory<State, Action> {
        override fun inflate(parent: ViewGroup): ViewRenderer<State, Action> {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(layoutId, parent, false)
            return create(view)
        }

        protected abstract fun create(rootView: View): ViewRenderer<State, Action>
    }

    abstract class ViewBindingFactory<Binding : ViewBinding, State, Action>(
        private val bindingFactory: (LayoutInflater, ViewGroup, Boolean) -> Binding
    ) : InflaterFactory<State, Action> {
        override fun inflate(parent: ViewGroup): ViewRenderer<State, Action> {
            val inflater = LayoutInflater.from(parent.context)
            val binding = bindingFactory(inflater, parent, false)
            return create(binding)
        }

        protected abstract fun create(binding: Binding): ViewRenderer<State, Action>
    }
}
