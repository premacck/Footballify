package life.plank.juna.zone.data.api

import android.util.Log
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.ui.base.fragment.BaseJunaFragment
import life.plank.juna.zone.util.common.errorToast
import okhttp3.*
import org.jetbrains.anko.runOnUiThread
import rx.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File

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

fun <T> Observable<T>.onSubscribe(onSubscribe: () -> Unit): Observable<T> = doOnSubscribe { ZoneApplication.appContext.runOnUiThread { onSubscribe() } }

fun <T> Observable<T>.onTerminate(onSubscribe: () -> Unit): Observable<T> = doOnTerminate { ZoneApplication.appContext.runOnUiThread { onSubscribe() } }

fun File.createMultiPartImage(partName: String = ""): MultipartBody.Part =
        MultipartBody.Part.createFormData(partName, name, RequestBody.create(MediaType.parse(findString(R.string.media_type_image)), this))

fun String.createRequestBody(): RequestBody = RequestBody.create(MediaType.parse(findString(R.string.text_content_type)), this)