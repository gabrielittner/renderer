package com.gabrielittner.renderer.connect

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.gabrielittner.renderer.Renderer
import io.reactivex.disposables.Disposable

internal class RendererActionObserver<State : Any, Action : Any>(
    private val renderer: Renderer<State, Action>,
    private val actionHandler: (Action) -> Unit
) : DefaultLifecycleObserver {

    private var disposable: Disposable? = null

    override fun onStart(owner: LifecycleOwner) {
        disposable = renderer.actions.subscribe(
            { actionHandler(it) },
            { crashApp(exceptionMessage(it, owner), it) }
        )
    }

    override fun onStop(owner: LifecycleOwner) {
        disposable?.dispose()
        disposable = null
    }

    private fun crashApp(message: String, cause: Throwable) {
        val exception = RendererConnectionException(message, cause)
        val thread = Thread.currentThread()
        val handler = thread.uncaughtExceptionHandler
        if (handler != null) {
            handler.uncaughtException(thread, exception)
        } else {
            throw exception
        }
    }

    private fun exceptionMessage(t: Throwable, owner: LifecycleOwner): String {
        return "Received ${t::class.java.simpleName} from ${renderer::class.java.simpleName} " +
            "while lifecycle state is ${owner.lifecycle.currentState}"
    }
}
