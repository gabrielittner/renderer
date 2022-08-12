package com.gabrielittner.renderer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
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
abstract class ComposeRenderer<State : Any, Action : Any>(
    val view: ComposeView
) : ViewRenderer<State, Action>(view) {

    override fun renderToView(state: State) {
        view.setContent {
            Render(state)
        }
    }

    @Composable
    abstract fun Render(state: State)

    /**
     * A factory that creates a [ComposeRenderer].
     */
    abstract class Factory<R : ComposeRenderer<*, *>>() : BaseFactory<R> {
        /**
         * Creates a [ComposeRenderer] for [parent].
         */
        final override fun inflate(parent: ViewGroup): R {
            val view = ComposeView(parent.context)
            return create(view)
        }

        /**
         * Create a [ComposeRenderer] for the given [view].
         */
        protected abstract fun create(view: ComposeView): R
    }
}
