package com.gabrielittner.renderer.connect

import androidx.lifecycle.LifecycleOwner
import com.gabrielittner.renderer.Renderer
import io.reactivex.disposables.Disposable

class RendererActionObserver<State, Action>(
    private val renderer: Renderer<State, Action>,
    private val actionHandler: (Action) -> Unit
) : BaseObserver() {

    override fun createDisposable(owner: LifecycleOwner): Disposable {
        return renderer.actions.subscribe(
            { actionHandler(it) },
            { crashApp(exceptionMessage(it, owner), it) }
        )
    }

    private fun exceptionMessage(t: Throwable, owner: LifecycleOwner): String {
        return "Received ${t::class.java.simpleName} from ${renderer::class.java.simpleName} " +
            "while lifecycle state is ${owner.lifecycle.currentState}"
    }
}
