package life.plank.juna.zone.util

import android.annotation.SuppressLint
import android.support.annotation.StringRes
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.RestApiAggregator
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.DataUtil.*
import life.plank.juna.zone.util.common.launchMatchBoard
import life.plank.juna.zone.util.common.launchPrivateBoard
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.uiThread
import retrofit2.Response
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.HttpURLConnection.HTTP_OK

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
    return subscribe(object : Subscriber<T>() {
                override fun onCompleted() {}

                override fun onError(e: Throwable) {
                    errorToast(R.string.something_went_wrong, e)
                    onError(e)
                }

                override fun onNext(t: T) = onNext(t)
            })
}

fun RestApi.launchPrivateBoard(boardId: String, baseCardActivity: BaseCardActivity) {
    RestApiAggregator.getPrivateBoardToOpen(boardId, this).smartSubscribe({
        Log.e("launchPrivateBoard", "onError(): ", it)
        customToast(R.string.could_not_navigate_to_board)
    }, {
        it?.run { baseCardActivity.launchPrivateBoard(this) }
    })
}

fun RestApi.launchMatchBoard(matchId: Long, baseCardActivity: BaseCardActivity, leagueName: String = "") {
    if (!isNullOrEmpty(leagueName)) {
        getMatchDetails(matchId).setObserverThreadsAndSmartSubscribe({ Log.e("launchMatchBoard", "onError(): ", it) }, {
            when (it.code()) {
                HTTP_OK -> {
                    val matchDetails = it.body()!!
                    doAsync {
                        matchDetails.league = getSpecifiedLeague(leagueName)
                        uiThread { baseCardActivity.launchMatchBoard(matchDetails) }
                    }
                }
                else -> errorToast(R.string.failed_to_get_match_details, it)
            }
        })
    }
}

fun customToast(@StringRes message: Int) = getCustomToast(findString(message), Toast.LENGTH_SHORT).show()

fun errorToast(@StringRes prependMessage: Int, response: Response<*>) = getCustomToast("${findString(prependMessage)}\n\nCode: ${response.code()}\nMessage: ${response.body()}").show()

fun errorToast(@StringRes prependMessage: Int, error: Throwable) = getCustomToast("${findString(prependMessage)}\n\n${error.message!!}").show()

fun errorLog(tag: String, @StringRes prependMessage: Int, response: Response<*>) = Log.e(tag, findString(prependMessage) + " Code: " + response.code() + " Message: " + response.message())

@SuppressLint("InflateParams")
fun getCustomToast(message: CharSequence, duration: Int = Toast.LENGTH_LONG): Toast {
    val toast = Toast(ZoneApplication.getContext())
    toast.duration = duration
    toast.view = ZoneApplication.getContext().layoutInflater.inflate(R.layout.item_toast, null)
    val toastTextView = toast.view.findViewById<TextView>(R.id.toast_text_view)
    toastTextView.text = message
    return toast
}