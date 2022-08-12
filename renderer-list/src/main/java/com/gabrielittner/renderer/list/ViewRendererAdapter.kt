package com.gabrielittner.renderer.list

import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gabrielittner.renderer.Renderer
import com.gabrielittner.renderer.ViewRenderer
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

abstract class ViewRendererAdapter<State : Any, Action : Any>(
    callback: DiffUtil.ItemCallback<State>
) : AsyncListDifferDelegationAdapter<State>(callback) {

    private val scope = MainScope()
    private val actions = MutableSharedFlow<Action>(extraBufferCapacity = Int.MAX_VALUE)
    private val jobs = mutableMapOf<RecyclerView.ViewHolder, Job>()

    fun actions(): Flow<Action> = actions

    @CallSuper
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        jobs[holder] = scope.launch {
            holder.renderer.actions.collect(actions::emit)
        }
    }

    @CallSuper
    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        jobs.remove(holder)!!.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    private val RecyclerView.ViewHolder.renderer: Renderer<State, Action>
        get() = itemView.getTag(R.id.view_renderer_adapter_item_tag) as Renderer<State, Action>

    protected inline fun <reified StateSubtype : State> addRendererDelegate(
        factory: ViewRenderer.BaseFactory<out ViewRenderer<StateSubtype, Action>>,
        noinline on: (item: StateSubtype, items: List<State>, position: Int) -> Boolean = { _, _, _ -> true },
    ) {
        val viewTypeId = View.generateViewId()
        val delegate = adapterDelegate<StateSubtype, State>(
            viewTypeId,
            on = { item, items, position -> item is StateSubtype && on(item, items, position) },
            layoutInflater = { parent: ViewGroup, _: Int ->
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
