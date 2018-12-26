package life.plank.juna.zone.util.common

import android.annotation.SuppressLint
import android.util.Log
import android.widget.*
import androidx.annotation.StringRes
import life.plank.juna.zone.*
import life.plank.juna.zone.util.common.JunaDataUtil.findString
import life.plank.juna.zone.view.fragment.base.BaseJunaFragment
import org.jetbrains.anko.*
import retrofit2.Response
import rx.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

fun <T> Observable<T>.setObserverThreadsAndSubscribe(subscriber: Subscriber<in T>): Subscription {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber)
}

fun <T> Observable<T>.setObserverThreadsAndSmartSubscribe(onError: (e: Throwable) -> Unit, onNext: (t: T) -> Unit, fragmentToAttach: Any? = null): Subscription {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .smartSubscribe(fragmentToAttach, onError, onNext)
}

fun <T> Observable<T>.execute(fragmentToAttach: Any? = null): Subscription {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .smartSubscribe(fragmentToAttach, {
                Log.e("execute(): ", this@execute.toString(), it)
                errorToast(R.string.something_went_wrong, it)
            }, {})
}

fun <T> Observable<T>.smartSubscribe(fragmentToAttach: Any? = null, onError: (e: Throwable) -> Unit, onNext: (t: T) -> Unit): Subscription {
    val subscription = subscribe(object : Subscriber<T>() {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            errorToast(R.string.something_went_wrong, e)
            (fragmentToAttach as? BaseJunaFragment)?.run {
                if (isAdded) onError(e)
            } ?: onError(e)
        }

        override fun onNext(t: T) {
            (fragmentToAttach as? BaseJunaFragment)?.run {
                if (isAdded) onNext(t)
            } ?: onNext(t)
        }
    })
    (fragmentToAttach as? BaseJunaFragment)?.run {
        if (!subscriptionList.contains(subscription)) {
            subscriptionList.add(subscription)
        }
    }
    return subscription
}

fun <T> Observable<T>.onSubscribe(onSubscribe: () -> Unit): Observable<T> = doOnSubscribe { ZoneApplication.getContext().runOnUiThread { onSubscribe() } }

fun <T> Observable<T>.onTerminate(onSubscribe: () -> Unit): Observable<T> = doOnTerminate { ZoneApplication.getContext().runOnUiThread { onSubscribe() } }

fun customToast(@StringRes message: Int) = getCustomToast(findString(message), Toast.LENGTH_SHORT)?.show()

fun errorToast(@StringRes prependMessage: Int, response: Response<*>) = getCustomToast("${findString(prependMessage)}\n\nCode: ${response.code()}\nMessage: ${response.body()}")?.show()

fun errorToast(@StringRes prependMessage: Int, error: Throwable) = getCustomToast("${findString(prependMessage)}\n\n${error.message!!}")?.show()

fun errorLog(tag: String, @StringRes prependMessage: Int, response: Response<*>) = Log.e(tag, findString(prependMessage) + " Code: " + response.code() + " Message: " + response.message())

@SuppressLint("InflateParams")
fun getCustomToast(message: CharSequence, duration: Int = Toast.LENGTH_LONG): Toast? {
    ZoneApplication.getContext().run {
        var toast: Toast? = null
        runOnUiThread {
            toast = Toast(this)
            toast?.duration = duration
            toast?.view = layoutInflater.inflate(R.layout.item_toast, null)
            val toastTextView = toast?.view?.findViewById<TextView>(R.id.toast_text_view)
            toastTextView?.text = message
        }
        return toast
    }
}