package com.gabrielittner.binder.connect

internal class BinderConnectionException(
    message: String,
    cause: Throwable
) : RuntimeException(message, cause)
