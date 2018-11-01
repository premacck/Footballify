package life.plank.juna.zone.util

import android.support.v4.app.FragmentManager
import android.util.Log
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.RestApiAggregator
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.facilis.launchPrivateBoard
import org.jetbrains.anko.toast
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

fun <T> Observable<T>.setObserverThreadsAndSmartSubscribe(onError: (e: Throwable) -> Unit, onNext: (t: T) -> Unit): Subscription {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .smartSubscribe(onError, onNext)
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

fun launchPrivateBoard(boardId: String, restApi: RestApi, resId: Int, fragmentManager: FragmentManager) {
    RestApiAggregator.getPrivateBoardToOpen(boardId, restApi).smartSubscribe({
        Log.e("launchPrivateBoard", "onError(): ", it)
        ZoneApplication.getContext().toast(R.string.could_not_navigate_to_board)
    }, {
        it?.run {
            fragmentManager.launchPrivateBoard(resId, this)
        }
    })
}