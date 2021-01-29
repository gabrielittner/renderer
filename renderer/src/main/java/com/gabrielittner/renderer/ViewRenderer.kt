package com.gabrielittner.renderer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.viewbinding.ViewBinding
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * A base class for [Renderer] implementations that use Android [View]. Compared to the simple
 * [Renderer] interface this provides some convenience methods l
 */
@UiThread
abstract class ViewRenderer<State, Action>(
    val rootView: View
) : Renderer<State, Action> {

    /**
     * Create a [ViewRenderer] from a [ViewBinding].
     */
    constructor(binding: ViewBinding) : this(binding.root)

    private var internalState: State? = null

    /**
     * Access the currently rendered state. This can be used if there are [Action] objects
     * that require parts of the current state to be instantiated.
     *
     * Will throw an [IllegalStateException] if called before the first [renderToView] invocation.
     */
    protected val state: State get() {
        if (internalState == null) {
            throw IllegalStateException("State is only available after the first renderToView call")
        }
        return internalState!!
    }

    private val internalActions: PublishSubject<Action> = PublishSubject.create()

    /**
     * See [Renderer.actions]. Implementations should override [viewActions] instead or use
     * [sendAction].
     */
    final override val actions: Observable<Action>
        get() = Observable.merge(internalActions, viewActions)

    /**
     * See [Renderer.actions].
     *
     * Using RxBinding an implementation could look like this:
     * ```
     * val viewActions = Observable.merge(
     *   toolbar.navigationClicks().map { ToolbarNavigationClicked },
     *   button.clicks().map { ButtonClicked },
     *   editText.textChanges().map { TextChanged(it) }
     * )
     * ```
     */
    protected open val viewActions: Observable<Action> = Observable.never()

    /**
     * Emits the given [action] to observers of the [actions] `Observable`. The main use case for
     * this is to emit actions for objects that are not available for the whole lifetime of the
     * Renderer, for example a dialog click.
     */
    protected fun sendAction(action: Action) {
        internalActions.onNext(action)
    }

    /**
     * See [Renderer.render]. Implementations should override [renderToView] instead.
     */
    final override fun render(state: State) {
        internalState = state
        renderToView(state)
    }

    /**
     * See [Renderer.render].
     */
    protected abstract fun renderToView(state: State)

    /**
     * A factory that inflates the a [ViewBinding] using the given [bindingFactory].
     */
    abstract class Factory<Binding : ViewBinding, R : ViewRenderer<*, *>>(
        private val bindingFactory: (LayoutInflater, ViewGroup?, Boolean) -> Binding
    ) {
        /**
         * Inflates a [ViewBinding] using [bindingFactory] in the given [parent] and then creates
         * a [ViewRenderer] for it.
         */
        fun inflate(parent: ViewGroup): R {
            val inflater = LayoutInflater.from(parent.context)
            return inflate(inflater, parent)
        }

        /**
         * Inflates a [ViewBinding] using [bindingFactory] in the given [parent] and then creates
         * a [ViewRenderer] for it.
         */
        fun inflate(inflater: LayoutInflater, parent: ViewGroup?): R {
            val binding = bindingFactory(inflater, parent, false)
            return create(binding)
        }

        /**
         * Create a [ViewBindingRenderer] for the given [binding].
         */
        protected abstract fun create(binding: Binding): R
    }
}
