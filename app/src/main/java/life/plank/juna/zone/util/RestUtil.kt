package life.plank.juna.zone.util

import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

fun <T> Observable<T>.setObserverThreadsAndSubscribe(subscriber: Subscriber<in T>): Subscription {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber)
}

fun <T> Observable<T>.execute(): Subscription {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
}

fun <T> Observable<T>.smartSubscribe(onError: (e: Throwable) -> Unit, onNext: (t: T) -> Unit): Subscription {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<T>() {
                override fun onCompleted() {}

                override fun onError(e: Throwable) = onError(e)

                override fun onNext(t: T) = onNext(t)
            })
}