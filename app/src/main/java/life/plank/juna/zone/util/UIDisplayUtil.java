package life.plank.juna.zone.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.ListPopupWindow;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by plank-sobia on 11/8/2017.
 */

public class UIDisplayUtil {

    private UIDisplayUtil() {

    }

    public static UIDisplayUtil getInstance() {
        return UIDisplayUtilWrapper.INSTANCE;
    }

    /**
     * Dp to pixel conversion.
     *
     * @param dp : dp to be converted
     * @return pixel : Converted value.
     */
    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * Common function to get Display metrics data
     *
     * @param context : context
     * @param status  : Request for particular data
     * @return integer : bases on status
     */
    public static int getDisplayMetricsData(Context context, int status) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        switch (status) {
            case GlobalVariable.DISPLAY_HEIGHT:
                return displayMetrics.heightPixels;
            case GlobalVariable.DISPLAY_WIDTH:
                return displayMetrics.widthPixels;
            default:
                return GlobalVariable.getInstance().getDisplayMetricsErrorState();
        }
    }

    public void displaySnackBar(View currentView, String message) {
        Snackbar.make(currentView, message, Snackbar.LENGTH_LONG).show();
    }

    public void hideSoftKeyboard(View view, Context context) {
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private static class UIDisplayUtilWrapper {
        private static final UIDisplayUtil INSTANCE = new UIDisplayUtil();
    }

    public  void dismissPopupListWindow(ListPopupWindow listPopupWindow) {
        if (listPopupWindow != null && listPopupWindow.isShowing())
            listPopupWindow.dismiss();
    }
}
