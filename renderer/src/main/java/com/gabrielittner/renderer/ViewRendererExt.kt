package com.gabrielittner.renderer

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

val ViewRenderer<*, *>.context get(): Context = rootView.context

fun <T : View> ViewRenderer<*, *>.bind(@IdRes id: Int): T = rootView.findViewById(id)

fun <State, Action> ViewRenderer.Factory<State, Action>.create(
    fragment: Fragment
): Renderer<State, Action> {
    return create(fragment.view!!)
}

fun <State, Action> ViewRenderer.Factory<State, Action>.create(
    activity: FragmentActivity
): Renderer<State, Action> {
    return create(activity.findViewById(android.R.id.content))
}
