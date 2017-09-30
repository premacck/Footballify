package life.plank.juna.zone.util;

import android.content.res.Resources;
import android.os.Build;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by plank-sobia on 9/30/2017.
 */

public class CustomizeStatusBar {

    public static void setTransparentStatusBarColor(Resources.Theme theme, Window activityWindow) {
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true);
        if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            int color = typedValue.data;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activityWindow.setStatusBarColor(color);
            }
        }
    }

    public static void removeStatusBar(Window activityWindow) {
        activityWindow.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
