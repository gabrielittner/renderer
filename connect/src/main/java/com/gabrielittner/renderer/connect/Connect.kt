package com.gabrielittner.renderer.connect

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.freeletics.mad.statemachine.StateMachine
import com.gabrielittner.renderer.Renderer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <State : Any, Action : Any> Fragment.connect(
    renderer: Renderer<State, Action>,
    model: StateMachine<State, Action>
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

fun <State : Any, Action : Any> LifecycleOwner.connect(
    renderer: Renderer<State, Action>,
    stateMachine: StateMachine<State, Action>
) {
    lifecycle.addObserver(WhileStartedObserver {
        it.launch { stateMachine.state.collect { renderer.render(it) } }
        it.launch { renderer.actions.collect { stateMachine.dispatch(it) } }
    })
}
