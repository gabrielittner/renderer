package com.gabrielittner.binder.connectors

import androidx.lifecycle.LiveData

interface LiveDataViewModel<State, Action> {
    fun handleAction(action: Action)
    fun observe(): LiveData<State>
}
