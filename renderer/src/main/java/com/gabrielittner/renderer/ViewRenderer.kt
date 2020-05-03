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
        fun inflate(
            inflater: LayoutInflater,
            parent: ViewGroup?,
            attachToRoot: Boolean
        ): ViewRenderer<State, Action>
    }

    abstract class LayoutInflaterFactory<State, Action> : InflaterFactory<State, Action> {
        final override fun inflate(
            inflater: LayoutInflater,
            parent: ViewGroup?,
            attachToRoot: Boolean
        ): ViewRenderer<State, Action> {
            val view = inflater.inflate(layoutId, parent, attachToRoot)
            return create(view)
        }

        protected abstract val layoutId: Int

        protected abstract fun create(rootView: View): ViewRenderer<State, Action>
    }

    abstract class ViewBindingFactory<Binding : ViewBinding, State, Action> : InflaterFactory<State, Action> {
        final override fun inflate(
            inflater: LayoutInflater,
            parent: ViewGroup?,
            attachToRoot: Boolean
        ): ViewRenderer<State, Action> {
            val binding = create(inflater, parent, attachToRoot)
            return create(binding)
        }

        protected abstract fun create(inflater: LayoutInflater, parent: ViewGroup?, attachToRoot: Boolean): Binding

        protected abstract fun create(binding: Binding): ViewRenderer<State, Action>
    }
}
