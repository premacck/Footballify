package life.plank.juna.zone.util.view;

import java.util.HashMap;

import life.plank.juna.zone.R;

import static life.plank.juna.zone.util.common.DataUtil.findString;

public class DrawableHashMap {

    private static HashMap<String, Integer> drawableMap = new HashMap<>();

    static {
        drawableMap.put(findString(R.string.blue_color), R.drawable.blue_gradient);
        drawableMap.put(findString(R.string.purple_color), R.drawable.purple_gradient);
        drawableMap.put(findString(R.string.green_color), R.drawable.green_gradient);
        drawableMap.put(findString(R.string.orange_color), R.drawable.orange_gradient);
    }

    public static HashMap<String, Integer> getDrawableMap() {
        return drawableMap;
    }
}