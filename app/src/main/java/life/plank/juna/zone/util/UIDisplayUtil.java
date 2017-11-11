package life.plank.juna.zone.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
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
}
