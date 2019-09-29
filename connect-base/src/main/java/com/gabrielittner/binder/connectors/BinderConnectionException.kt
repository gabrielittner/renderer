package com.gabrielittner.binder.connectors

internal class BinderConnectionException(
    message: String,
    cause: Throwable
) : RuntimeException(message, cause)
