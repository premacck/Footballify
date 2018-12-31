package com.prembros.asymmetricrecyclerview.base

import android.content.Context
import android.util.*
import android.view.WindowManager
import androidx.annotation.ColorRes

@Suppress("DEPRECATION")
object MasonryUtils {

    /**
     * Returns a valid DisplayMetrics object
     *
     * @receiver valid context
     * @return DisplayMetrics object
     */
    private fun Context.getDisplayMetrics(): DisplayMetrics {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        return metrics
    }

    internal fun getScreenWidth(context: Context?): Int = context?.getDisplayMetrics()?.widthPixels
            ?: 0

    fun Context.getDp(dp: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)

    fun Context.findColor(@ColorRes color: Int): Int = resources.getColor(color)
}
