package com.prembros.asymmetricrecyclerview.pool

interface PoolObjectFactory<T> {
    fun createObject(): T
}
