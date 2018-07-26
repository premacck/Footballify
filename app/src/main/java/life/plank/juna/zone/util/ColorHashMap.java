package life.plank.juna.zone.util;

import android.content.Context;

import java.util.HashMap;

import life.plank.juna.zone.R;

public class ColorHashMap {

    private static HashMap<String, Integer> colorMap;

    public static void HashMaps(Context context) {
        colorMap = new HashMap<>();
        colorMap.put(context.getString(R.string.red), R.color.red);
        colorMap.put(context.getString(R.string.pink), R.color.material_pink_800);
        colorMap.put(context.getString(R.string.yellow), R.color.material_yellow_700);
        colorMap.put(context.getString(R.string.green), R.color.material_green_700);
        colorMap.put(context.getString(R.string.blue), R.color.material_blue_600);

    }

    public static HashMap<String, Integer> getColorMapMap() {
        return colorMap;
    }

}
