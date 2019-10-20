package com.gabrielittner.renderer.connect

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

abstract class RxLiveDataViewModel<State, Action> :
    LiveDataViewModel<State, Action> {

    private val liveData: MutableLiveData<State> =
        MutableLiveData()

    private val actionSubject: PublishSubject<Action> =
        PublishSubject.create()

    protected val actions: Observable<Action> = actionSubject.hide()

    final override fun handleAction(action: Action) {
        actionSubject.onNext(action)
    }

    protected fun updateState(state: State) {
        liveData.value = state
    }

    final override fun observe(): LiveData<State> = liveData
}
