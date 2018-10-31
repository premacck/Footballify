package com.prembros.asymmetricrecyclerview.base

import android.view.View

interface AsymmetricRecyclerViewListener {

    fun fireOnItemClick(index: Int, v: View)

    fun fireOnItemLongClick(index: Int, v: View): Boolean
}