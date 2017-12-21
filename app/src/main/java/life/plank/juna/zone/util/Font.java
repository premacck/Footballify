package life.plank.juna.zone.util;

import android.content.res.AssetManager;
import android.graphics.Typeface;

/**
 * Created by plank-arfaa on 21/12/17.
 */

public class Font {

    public static Typeface getFont(String fontName, AssetManager asset){
        return Typeface.createFromAsset(asset, fontName);
    }
}
