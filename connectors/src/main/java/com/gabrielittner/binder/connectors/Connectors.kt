package com.gabrielittner.binder.connectors

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.gabrielittner.binder.Binder
import com.gabrielittner.binder.ViewBinder
import com.gabrielittner.binder.create
import io.reactivex.disposables.Disposable

fun <State, Action> Fragment.connect(
    uiFactory: ViewBinder.Factory<State, Action>,
    model: ViewStateModel<State, Action>
) {
    val ui = uiFactory.create(this)
    return connect(ui, model)
}

fun <State, Action> FragmentActivity.connect(
    uiFactory: ViewBinder.Factory<State, Action>,
    model: ViewStateModel<State, Action>
) {
    val ui = uiFactory.create(this)
    return connect(ui, model)
}

fun <State, Action> LifecycleOwner.connect(
    binder: Binder<State, Action>,
    model: ViewStateModel<State, Action>
) {
    return lifecycle.connect(binder, model)
}

fun <State, Action> Lifecycle.connect(
    binder: Binder<State, Action>,
    model: ViewStateModel<State, Action>
) {
    val observer = WhileStartedObserver(binder, model)
    addObserver(observer)
}

private class WhileStartedObserver<State, Action>(
    private val binder: Binder<State, Action>,
    private val model: ViewStateModel<State, Action>
) : DefaultLifecycleObserver {
    var disposable: Disposable? = null

    override fun onStart(owner: LifecycleOwner) {
        disposable = model.observe(binder.actions)
            .subscribe(
                { binder.render(it) },
                { throw BinderConnectionException(
                    exceptionMessage(it, owner),
                    it
                )
                }
            )
    }

    override fun onStop(owner: LifecycleOwner) {
        disposable?.dispose()
        disposable = null
    }

    private fun exceptionMessage(throwable: Throwable, owner: LifecycleOwner): String {
        return "Received ${throwable::class.java.simpleName} from upstream " +
                "${model::class.java.simpleName} rendering into ${binder::class.java.simpleName} " +
                "while lifecycle state is ${owner.lifecycle.currentState}"
    }
}

private class BinderConnectionException(
    message: String,
    cause: Throwable
) : RuntimeException(message, cause)
