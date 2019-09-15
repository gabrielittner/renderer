package com.gabrielittner.binder.list

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gabrielittner.binder.Binder
import com.gabrielittner.binder.ViewBinder
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

abstract class ViewBinderAdapter<State, Action>(
    callback: DiffUtil.ItemCallback<State>
) : AsyncListDifferDelegationAdapter<State>(callback) {

    private val actionSubject = PublishSubject.create<Action>()
    private val disposables = mutableMapOf<RecyclerView.ViewHolder, Disposable>()

    fun actions(): Observable<Action> = actionSubject.hide()

    @CallSuper
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        disposables[holder] = holder.binder.actions.subscribe(actionSubject::onNext)
    }

    @CallSuper
    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        disposables.remove(holder)!!.dispose()
    }

    @Suppress("UNCHECKED_CAST")
    private val RecyclerView.ViewHolder.binder: Binder<State, Action>
        get() = itemView.getTag(R.id.view_binder_adapter_item_tag) as Binder<State, Action>

    protected inline fun <reified StateSubtype : State> addBinderDelegate(
        layout: Int,
        factory: ViewBinder.Factory<StateSubtype, Action>
    ) {
        val delegate = adapterDelegate<StateSubtype, State>(layout) {
            val binder = factory.create(itemView)
            itemView.setTag(R.id.view_binder_adapter_item_tag, binder)

            bind {
                binder.render(item)
            }
        }
        delegatesManager.addDelegate(delegate)
    }
}
