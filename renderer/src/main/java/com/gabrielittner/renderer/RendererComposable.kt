package com.gabrielittner.renderer

import android.view.LayoutInflater
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun <State : Any, Action : Any> Renderer(
    factory: ViewRenderer.Factory<*, out ViewRenderer<State, Action>>,
    state: State,
    sendAction: (Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    val rendererState = remember { mutableStateOf<ViewRenderer<State, Action>?>(null) }

    // Without this extra variable the LaunchedEffect will run with key = null and rendererState.value
    // can be updated by the time the block runs. That would result in first starting to collect the actions
    // and then after the re-composition that changes the key to be not null it will stop the collection and 
    // start again.
    val currentRenderer = rendererState.value
    if (currentRenderer != null) {
        LaunchedEffect(currentRenderer) {
            currentRenderer.actions.collect { sendAction(it) }
        }
    }

    AndroidView(
        modifier = modifier,
        factory = {
            val newRenderer = factory.inflate(LayoutInflater.from(it), null)
            rendererState.value = newRenderer
            newRenderer.rootView
        },
    ) {
        rendererState.value!!.render(state)
    }
}
