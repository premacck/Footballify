package life.plank.juna.zone.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import life.plank.juna.zone.R;

import static life.plank.juna.zone.ZoneApplication.getContext;
import static life.plank.juna.zone.util.UIDisplayUtil.getScreenshot;

/**
 * Class for handling file management, i.e., saving and retrieving a file/image from the app's private storage
 */
public class FileHandler {

    public static void saveScreenshot(String activityTag, View screenshotView, Intent intent) {
        String fileName = activityTag + getContext().getString(R.string.screenshot_background_suffix);
        intent.putExtra(getContext().getString(R.string.intent_activity_name), activityTag);
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

    public static Bitmap getSavedScreenshot(Intent intent) {
        String fileName = intent.getStringExtra(getContext().getString(R.string.intent_activity_name)) + getContext().getString(R.string.screenshot_background_suffix);
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