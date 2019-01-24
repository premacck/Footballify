package life.plank.juna.zone.util.common

import android.annotation.SuppressLint
import android.util.Log
import android.widget.*
import androidx.annotation.StringRes
import life.plank.juna.zone.*
import life.plank.juna.zone.service.CommonDataService
import org.jetbrains.anko.*
import retrofit2.Response

fun customToast(@StringRes message: Int) = getCustomToast(CommonDataService.findString(message), Toast.LENGTH_SHORT)?.show()

fun errorToast(@StringRes prependMessage: Int, response: Response<*>) = getCustomToast("${CommonDataService.findString(prependMessage)}\n\nCode: ${response.code()}\nMessage: ${response.body()}")?.show()

fun errorToast(@StringRes prependMessage: Int, error: Throwable) = getCustomToast("${CommonDataService.findString(prependMessage)}\n\n${error.message!!}")?.show()

fun errorLog(tag: String, @StringRes prependMessage: Int, response: Response<*>) = Log.e(tag, CommonDataService.findString(prependMessage) + " Code: " + response.code() + " Message: " + response.message())

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