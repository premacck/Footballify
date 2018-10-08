package life.plank.juna.zone.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import static life.plank.juna.zone.ZoneApplication.getContext;
import static life.plank.juna.zone.util.UIDisplayUtil.getScreenshot;

public class FileHandler {

    public static void saveScreenshot(String activityTag, View screenshotView) {
        String fileName = activityTag + "_bg.png";
        try {
            FileOutputStream fileOutputStream = getContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            Bitmap screenshot = getScreenshot(screenshotView);
            screenshot.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
            screenshot.recycle();
        } catch (Exception e) {
            Log.e(fileName, "saveScreenshot() : ", e);
        }
    }

    public static Bitmap getSavedScreenshot(String activityTag) {
        String fileName = activityTag + "_bg.png";
        try {
            FileInputStream fileInputStream = getContext().openFileInput(fileName);
            Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
            fileInputStream.close();
            return bitmap;
        } catch (Exception e) {
            Log.e(fileName, "getSavedBitmap() : ", e);
            return null;
        }
    }
}