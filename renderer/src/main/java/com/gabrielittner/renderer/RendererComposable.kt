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

    LaunchedEffect(rendererState.value) {
        rendererState.value?.actions?.collect { sendAction(it) }
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
