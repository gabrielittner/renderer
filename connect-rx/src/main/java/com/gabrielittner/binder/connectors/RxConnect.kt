package com.gabrielittner.binder.connectors

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.gabrielittner.binder.Binder
import com.gabrielittner.binder.ViewBinder
import com.gabrielittner.binder.create

fun <State, Action> Fragment.connect(
    binderFactory: ViewBinder.Factory<State, Action>,
    model: RxViewModel<State, Action>
) {
    val binder = binderFactory.create(this)
    return connect(binder, model)
}

fun <State, Action> FragmentActivity.connect(
    binderFactory: ViewBinder.Factory<State, Action>,
    model: RxViewModel<State, Action>
) {
    val binder = binderFactory.create(this)
    return connect(binder, model)
}

fun <State, Action> LifecycleOwner.connect(
    binder: Binder<State, Action>,
    model: RxViewModel<State, Action>
) {
    return lifecycle.connect(binder, model)
}

fun <State, Action> Lifecycle.connect(
    binder: Binder<State, Action>,
    model: RxViewModel<State, Action>
) {
    val observer = RxWhileStartedObserver(binder, model)
    addObserver(observer)
}

private class RxWhileStartedObserver<State, Action>(
    private val binder: Binder<State, Action>,
    private val model: RxViewModel<State, Action>
) : WhileStartedObserver() {

    override fun onStart(owner: LifecycleOwner) {
        disposable = model.observe(binder.actions)
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
