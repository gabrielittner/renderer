package com.gabrielittner.renderer.connect

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.gabrielittner.renderer.Renderer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

internal class RendererActionObserver<State : Any, Action : Any>(
    private val renderer: Renderer<State, Action>,
    private val actionHandler: suspend (Action) -> Unit
) : DefaultLifecycleObserver {

    private var currentScope: CoroutineScope? = null

    override fun onStart(owner: LifecycleOwner) {
        currentScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        currentScope!!.launch {
            renderer.actions.collect(actionHandler)
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        currentScope?.cancel()
        currentScope = null
    }
}
