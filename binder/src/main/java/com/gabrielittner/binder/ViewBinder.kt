package com.gabrielittner.binder

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import io.reactivex.Observable

abstract class ViewBinder<State, Action>(
    val rootView: View
) : Binder<State, Action> {

    private var internalState: State? = null
    protected val state: State? get() = internalState

    abstract override val actions: Observable<Action>

    final override fun render(state: State) {
        internalState = state
        renderToView(state)
    }

    protected abstract fun renderToView(state: State)


    interface Factory<State, Action> {
        fun create(rootView: View): Binder<State, Action>
    }
}

val ViewBinder<*, *>.context get(): Context = rootView.context

fun <T : View> ViewBinder<*, *>.bind(@IdRes id: Int): T = rootView.findViewById(id)

fun <State, Event> ViewBinder.Factory<State, Event>.create(
    fragment: Fragment
): Binder<State, Event> {
    return create(fragment.view!!)
}

fun <State, Event> ViewBinder.Factory<State, Event>.create(
    activity: FragmentActivity
): Binder<State, Event> {
    return create(activity.findViewById(android.R.id.content))
}
