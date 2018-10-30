package life.plank.juna.zone.util.masonry.base

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

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
}
