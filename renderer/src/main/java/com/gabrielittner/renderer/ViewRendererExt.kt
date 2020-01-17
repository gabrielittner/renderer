package com.gabrielittner.renderer

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

val ViewRenderer<*, *>.context get(): Context = rootView.context

fun <T : View> ViewRenderer<*, *>.bind(@IdRes id: Int): T = rootView.findViewById(id)
