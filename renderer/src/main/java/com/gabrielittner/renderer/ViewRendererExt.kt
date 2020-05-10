package com.gabrielittner.renderer

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * Returns the [Context] of the renderer's [ViewRenderer.rootView].
 */
val ViewRenderer<*, *>.context get(): Context = rootView.context

/**
 * Finds and returns the [View] with the given [id] inside [ViewRenderer.rootView]. If no matching
 * `View` was found an `IllegalStateException` is thrown.
 */
fun <T : View> ViewRenderer<*, *>.bind(@IdRes id: Int): T = ViewCompat.requireViewById(rootView, id)

/**
 * Finds and returns the [View] with the given [id] inside [ViewRenderer.rootView] or `null` if
 * there is no matching `View`.
 */
fun <T : View> ViewRenderer<*, *>.bindOptional(@IdRes id: Int): T? = rootView.findViewById(id)
