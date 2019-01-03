package com.prembros.asymmetricrecyclerview.base

import android.view.View

interface AsymmetricView {

    val isDebugging: Boolean

    val numColumns: Int

    val isAllowReordering: Boolean

    val columnWidth: Int

    val dividerHeight: Int

    val requestedHorizontalSpacing: Int

    fun fireOnItemClick(index: Int, v: View)

    fun fireOnItemLongClick(index: Int, v: View): Boolean

    fun setOnItemPopupListener(index: Int, view: View) {}
}
