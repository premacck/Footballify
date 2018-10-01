package life.plank.juna.zone.util;

import android.content.Context;

import java.util.HashMap;

import life.plank.juna.zone.R;

public class DrawableHashMap {

    private static HashMap<String, Integer> drawableMap;

    public static void HashMaps(Context context) {
        drawableMap = new HashMap<>();
        drawableMap.put(context.getString(R.string.blue_color), R.drawable.blue_gradient);
        drawableMap.put(context.getString(R.string.purple_color), R.drawable.purple_gradient);
        drawableMap.put(context.getString(R.string.green_color), R.drawable.green_gradient);
        drawableMap.put(context.getString(R.string.orange_color), R.drawable.orange_gradient);
    }

    public static HashMap<String, Integer> getDrawableMap() {
        return drawableMap;
    }

}