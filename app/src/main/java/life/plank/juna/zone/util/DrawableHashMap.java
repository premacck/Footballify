package life.plank.juna.zone.util;

import android.content.Context;

import java.util.HashMap;

import life.plank.juna.zone.R;

public class DrawableHashMap {

    private static HashMap<String, Integer> drawableMap;

    public static void HashMaps(Context context) {
        drawableMap = new HashMap<>();
        drawableMap.put("blue_bg", R.drawable.blue_gradient);
        drawableMap.put("purple_bg", R.drawable.purple_gradient);
        drawableMap.put("green_bg", R.drawable.green_gradient);
        drawableMap.put("orange_bg", R.drawable.orange_gradient);
    }

    public static HashMap<String, Integer> getDrawableMap() {
        return drawableMap;
    }

}