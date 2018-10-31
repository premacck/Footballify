package com.prembros.asymmetricrecyclerview.base

import android.widget.ListAdapter

interface MasonryAdapter<T> : ListAdapter {

    fun addItem(item: T)

    fun appendItems(newItems: List<T>)

    fun setItems(items: List<T>)
}
