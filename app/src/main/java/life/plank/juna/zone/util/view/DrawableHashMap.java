package life.plank.juna.zone.util.view;

import android.graphics.drawable.Drawable;

import java.util.HashMap;

import life.plank.juna.zone.R;

import static android.graphics.drawable.GradientDrawable.Orientation.TR_BL;
import static life.plank.juna.zone.util.common.DataUtil.findString;
import static life.plank.juna.zone.util.facilis.ViewUtilKt.gradientOf;

public class DrawableHashMap {

    private static HashMap<String, Drawable> drawableMap = new HashMap<>();

    static {
        drawableMap.put(findString(R.string.blue_color), gradientOf(TR_BL, R.color.blue_gradient_one, R.color.blue_gradient_two));
        drawableMap.put(findString(R.string.purple_color), gradientOf(TR_BL, R.color.purple_gradient_one, R.color.purple_gradient_two));
        drawableMap.put(findString(R.string.green_color), gradientOf(TR_BL, R.color.green_gradient_one, R.color.green_gradient_two));
        drawableMap.put(findString(R.string.orange_color), gradientOf(TR_BL, R.color.orange_gradient_one, R.color.orange_gradient_two));
    }

    public static HashMap<String, Drawable> getDrawableMap() {
        return drawableMap;
    }
}