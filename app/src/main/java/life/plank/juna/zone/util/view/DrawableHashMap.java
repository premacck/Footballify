package life.plank.juna.zone.util.view;

import android.graphics.drawable.Drawable;

import java.util.HashMap;

import life.plank.juna.zone.R;

import static life.plank.juna.zone.util.common.DataUtil.findString;
import static life.plank.juna.zone.util.view.UIDisplayUtil.findDrawable;

public class DrawableHashMap {

    private static HashMap<String, Drawable> drawableMap = new HashMap<>();

    static {
        drawableMap.put(findString(R.string.blue_color), findDrawable(R.drawable.blue_gradient));
        drawableMap.put(findString(R.string.purple_color), findDrawable(R.drawable.purple_gradient));
        drawableMap.put(findString(R.string.green_color), findDrawable(R.drawable.green_gradient));
        drawableMap.put(findString(R.string.orange_color), findDrawable(R.drawable.orange_gradient));
    }

    public static HashMap<String, Drawable> getDrawableMap() {
        return drawableMap;
    }
}