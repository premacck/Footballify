package life.plank.juna.zone.util.masonry.pool

interface PoolObjectFactory<T> {
    fun createObject(): T
}
