package com.gabrielittner.renderer.connect

import androidx.lifecycle.LifecycleOwner
import com.gabrielittner.renderer.Renderer
import io.reactivex.disposables.Disposable

internal class RxWhileStartedObserver<State : Any, Action : Any>(
    private val binder: Renderer<State, Action>,
    private val model: RxStateMachine<State, Action>
) : BaseObserver() {

    override fun createDisposable(owner: LifecycleOwner): Disposable {
        return model.observe(binder.actions)
            .subscribe(
                { binder.render(it) },
                { crashApp(exceptionMessage(it, owner), it) }
            )
    }

    private fun exceptionMessage(throwable: Throwable, owner: LifecycleOwner): String {
        return "Received ${throwable::class.java.simpleName} from upstream " +
                "${model::class.java.simpleName} rendering into ${binder::class.java.simpleName} " +
                "while lifecycle state is ${owner.lifecycle.currentState}"
    }
}
