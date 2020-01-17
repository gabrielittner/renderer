package com.gabrielittner.renderer.connect

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.disposables.Disposable

abstract class BaseObserver : DefaultLifecycleObserver {
    private var disposable: Disposable? = null

    override fun onStart(owner: LifecycleOwner) {
        disposable = createDisposable(owner)
    }

    abstract fun createDisposable(owner: LifecycleOwner): Disposable

    override fun onStop(owner: LifecycleOwner) {
        disposable?.dispose()
        disposable = null
    }

    protected fun crashApp(message: String, cause: Throwable) {
        val exception = RendererConnectionException(message, cause)
        val thread = Thread.currentThread()
        val handler = thread.uncaughtExceptionHandler
        if (handler != null) {
            handler.uncaughtException(thread, exception)
        } else {
            throw exception
        }
    }
}
