package com.gabrielittner.binder.connectors

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.disposables.Disposable

abstract class WhileStartedObserver : DefaultLifecycleObserver {
    protected var disposable: Disposable? = null

    override fun onStop(owner: LifecycleOwner) {
        disposable?.dispose()
        disposable = null
    }

    protected fun crashApp(message: String, cause: Throwable) {
        val exception = BinderConnectionException(message, cause)
        val thread = Thread.currentThread()
        val handler = thread.uncaughtExceptionHandler
        if (handler != null) {
            handler.uncaughtException(thread, exception)
        } else {
            throw exception
        }
    }
}
