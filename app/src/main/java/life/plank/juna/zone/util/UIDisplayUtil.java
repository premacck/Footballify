package life.plank.juna.zone.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
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

    private static class UIDisplayUtilWrapper {
        private static final UIDisplayUtil INSTANCE = new UIDisplayUtil();
    }

    public static UIDisplayUtil getInstance() {
        return UIDisplayUtilWrapper.INSTANCE;
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

    /**
     * Dp to pixel conversion.
     * @param dp : dp to be converted
     * @return pixel : Converted value.
     */
    public static int dpToPx(int dp,Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * To get screen width.
     * @param context : Context
     * @return pixel : width of screen
     */
    public static int getScreenWidth(Context context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager =  (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        return displayMetrics.widthPixels;
    }

    /**
     * To get screen height.
     * @param context : Context
     * @return pixel : Height of screen
     */
    public static int getScreenHeight(Context context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager =  (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        return displayMetrics.heightPixels;
    }

}
