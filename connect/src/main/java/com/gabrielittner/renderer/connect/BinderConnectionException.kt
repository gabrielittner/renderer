package com.gabrielittner.renderer.connect

internal class RendererConnectionException(
    message: String,
    cause: Throwable
) : RuntimeException(message, cause)
