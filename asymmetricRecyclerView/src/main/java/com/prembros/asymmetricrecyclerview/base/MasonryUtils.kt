package com.prembros.asymmetricrecyclerview.base

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import androidx.annotation.ColorRes

@Suppress("DEPRECATION")
object MasonryUtils {

    internal fun getScreenWidth(context: Context?): Int {
        return if (context == null) {
            0
        } else getDisplayMetrics(context).widthPixels
    }

    /**
     * Returns a valid DisplayMetrics object
     *
     * @param context valid context
     * @return DisplayMetrics object
     */
    private fun getDisplayMetrics(context: Context): DisplayMetrics {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        return metrics
    }

    fun Context.getDp(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }

    fun Context.findColor(@ColorRes color: Int): Int {
        return resources.getColor(color)
    }
}
