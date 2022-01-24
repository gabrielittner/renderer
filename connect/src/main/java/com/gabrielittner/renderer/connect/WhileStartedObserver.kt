package com.gabrielittner.renderer.connect

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

internal class WhileStartedObserver(
    private val onScopeCreated: (CoroutineScope) -> Unit
) : DefaultLifecycleObserver {

    private var currentScope: CoroutineScope? = null

    override fun onStart(owner: LifecycleOwner) {
        currentScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        onScopeCreated(currentScope!!)
    }

    override fun onStop(owner: LifecycleOwner) {
        currentScope?.cancel()
        currentScope = null
    }
}
