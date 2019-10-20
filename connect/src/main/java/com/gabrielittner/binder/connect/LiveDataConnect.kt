package com.gabrielittner.binder.connect

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.gabrielittner.binder.Binder
import com.gabrielittner.binder.ViewBinder
import com.gabrielittner.binder.create

fun <State, Action> Fragment.connect(
    binderFactory: ViewBinder.Factory<State, Action>,
    model: LiveDataViewModel<State, Action>
) {
    val binder = binderFactory.create(this)
    return connect(binder, model)
}

fun <State, Action> FragmentActivity.connect(
    binderFactory: ViewBinder.Factory<State, Action>,
    model: LiveDataViewModel<State, Action>
) {
    val binder = binderFactory.create(this)
    return connect(binder, model)
}

fun <State, Action> LifecycleOwner.connect(
    binder: Binder<State, Action>,
    model: LiveDataViewModel<State, Action>
) {
    return lifecycle.connect(binder, model)
}

fun <State, Action> Lifecycle.connect(
    binder: Binder<State, Action>,
    model: LiveDataViewModel<State, Action>
) {
    val observer = LiveDataWhileStartedObserver(binder, model)
    addObserver(observer)
}

private class LiveDataWhileStartedObserver<State, Action>(
    private val binder: Binder<State, Action>,
    private val model: LiveDataViewModel<State, Action>
) : WhileStartedObserver() {

    override fun onStart(owner: LifecycleOwner) {
        disposable = binder.actions.subscribe(
            { model.handleAction(it) },
            { crashApp(exceptionMessage(it, owner), it) }
        )
        model.observe().observe(owner, Observer { binder.render(it) })
    }

    private fun exceptionMessage(throwable: Throwable, owner: LifecycleOwner): String {
        return "Received ${throwable::class.java.simpleName} from " +
                "${binder::class.java.simpleName} sending it to ${model::class.java.simpleName} " +
                "while lifecycle state is ${owner.lifecycle.currentState}"
    }
}
