package com.gabrielittner.renderer.connect

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.gabrielittner.renderer.Renderer

@Deprecated("Use RxLiveDataStateMachine instead")
typealias LiveDataViewModel<State, Action> = LiveDataStateMachine<State, Action>

interface LiveDataStateMachine<State, Action> {
    fun handleAction(action: Action)
    fun observe(): LiveData<State>
}

fun <State, Action> Fragment.connect(
    renderer: Renderer<State, Action>,
    model: LiveDataStateMachine<State, Action>
) {
    viewLifecycleOwnerLiveData.observe(this, object : Observer<LifecycleOwner> {
        override fun onChanged(viewLifecycleOwner: LifecycleOwner?) {
            if (viewLifecycleOwner != null) {
                viewLifecycleOwner.connect(renderer, model)
                // remove the observer after we got the first viewLifecycleOwner so that the
                // reference to renderer can be freed once the Fragment view get's destroyed
                viewLifecycleOwnerLiveData.removeObserver(this)
            }
        }
    })
}

fun <State, Action> LifecycleOwner.connect(
    renderer: Renderer<State, Action>,
    model: LiveDataStateMachine<State, Action>
) {
    lifecycle.addObserver(RendererActionObserver(renderer, model::handleAction))
    model.observe().observe(this, Observer { renderer.render(it) })
}
