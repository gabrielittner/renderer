package com.gabrielittner.renderer.list

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gabrielittner.renderer.Renderer
import com.gabrielittner.renderer.ViewRenderer
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

abstract class ViewRendererAdapter<State, Action>(
    callback: DiffUtil.ItemCallback<State>
) : AsyncListDifferDelegationAdapter<State>(callback) {

    private val actionSubject = PublishSubject.create<Action>()
    private val disposables = mutableMapOf<RecyclerView.ViewHolder, Disposable>()

    fun actions(): Observable<Action> = actionSubject.hide()

    @CallSuper
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        disposables[holder] = holder.renderer.actions.subscribe(actionSubject::onNext)
    }

    @CallSuper
    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        disposables.remove(holder)!!.dispose()
    }

    @Suppress("UNCHECKED_CAST")
    private val RecyclerView.ViewHolder.renderer: Renderer<State, Action>
        get() = itemView.getTag(R.id.view_renderer_adapter_item_tag) as Renderer<State, Action>

    protected inline fun <reified StateSubtype : State> addRendererDelegate(
        layout: Int,
        factory: ViewRenderer.Factory<StateSubtype, Action>
    ) {
        val delegate = adapterDelegate<StateSubtype, State>(layout) {
            val renderer = factory.create(itemView)
            itemView.setTag(R.id.view_renderer_adapter_item_tag, renderer)

            bind {
                renderer.render(item)
            }
        }
        delegatesManager.addDelegate(delegate)
    }

    protected inline fun <reified StateSubtype : State> addRendererDelegate(
        factory: ViewRenderer.InflaterFactory<StateSubtype, Action>
    ) {
        val delegate = adapterDelegate<StateSubtype, State>(
            factory.layoutId,
            layoutInflater = { parent, _ ->
                val renderer = factory.inflate(parent)
                renderer.rootView.setTag(R.id.view_renderer_adapter_item_tag, renderer)
                renderer.rootView
            }
        ) {
             @Suppress("UNCHECKED_CAST")
            val renderer = itemView.getTag(R.id.view_renderer_adapter_item_tag) as Renderer<State, Action>

            bind {
                renderer.render(item)
            }
        }
        delegatesManager.addDelegate(delegate)
    }
}
