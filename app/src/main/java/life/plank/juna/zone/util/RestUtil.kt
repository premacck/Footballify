package life.plank.juna.zone.util

import android.annotation.SuppressLint
import android.support.annotation.StringRes
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.util.DataUtil.findString
import org.jetbrains.anko.layoutInflater
import retrofit2.Response
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
            .subscribe(object : Subscriber<T>() {
                override fun onCompleted() {}
                override fun onNext(t: T) {}
                override fun onError(e: Throwable) {
                    Log.e("execute(): ", this@execute.toString(), e)
                    errorToast(R.string.something_went_wrong, e)
                }
            })
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