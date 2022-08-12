package com.gabrielittner.renderer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.viewbinding.ViewBinding
import io.reactivex.Observable
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.rx2.asFlow

/**
 * A base class for [Renderer] implementations that use Android [View]. Compared to the simple
 * [Renderer] interface this provides some convenience methods l
 */
@UiThread
abstract class ViewRenderer<State : Any, Action : Any>(
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

    private val sentActions = MutableSharedFlow<Action>(extraBufferCapacity = Int.MAX_VALUE)
    private val allActions = mutableListOf<Flow<Action>>(sentActions)
    private var allowedToAddActionFlows = true

    /**
     * See [Renderer.actions]. Implementations should override [viewActions] instead or use
     * [sendAction].
     */
    final override val actions: Flow<Action>
        get() {
            allowedToAddActionFlows = false

            var actions: List<Flow<Action>> = allActions

            val rxActions = viewActions
            if (rxActions != null) {
                actions = actions + rxActions.asFlow()
            }

            return actions.merge()
        }

    /**
     * Merges the given [flow] into the general [Action] [Flow] returned by [actions]. Only allowed
     * to be called before [actions] was called once.
     */
    protected fun addActionFlow(flow: Flow<Action>) {
        check(allowedToAddActionFlows) {
            "addActionFlow is only allowed to be called before actions was called"
        }

        allActions += flow
    }


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
    @Deprecated("Use sendAction or addActionFlow instead")
    protected open val viewActions: Observable<Action>? = null

    /**
     * Emits the given [action] to observers of the [actions] `Observable`. The main use case for
     * this is to emit actions for objects that are not available for the whole lifetime of the
     * Renderer, for example a dialog click.
     */
    protected fun sendAction(action: Action) {
        check(sentActions.tryEmit(action))
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
     * A factory that to create ViewRenderer.
     */
    interface BaseFactory<R : Renderer<*, *>> {
        /**
         * Inflates the [Renderer] using [parent].
         */
        fun inflate(parent: ViewGroup): R
    }

    /**
     * A factory that inflates the a [ViewBinding] using the given [bindingFactory].
     */
    abstract class Factory<Binding : ViewBinding, R : ViewRenderer<*, *>>(
        private val bindingFactory: (LayoutInflater, ViewGroup?, Boolean) -> Binding
    ) : BaseFactory<R> {
        /**
         * Inflates a [ViewBinding] using [bindingFactory] in the given [parent] and then creates
         * a [ViewRenderer] for it.
         */
        override fun inflate(parent: ViewGroup): R {
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
