package life.plank.juna.zone.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ListPopupWindow;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ToggleButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.SignInModel;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by plank-sobia on 11/8/2017.
 */

public class UIDisplayUtil {

    private static String SIGN_UP_USER_DETAILS = "signUpPageDetails";

    public UIDisplayUtil() {

    }

    public static int getCommentColor(String comment) {
        ColorHashMap.HashMaps(getApplicationContext());
        int color = ColorHashMap.getColorMapMap().get(comment.substring(0, comment.indexOf("$")));
        return getApplicationContext().getResources().getColor(color);
    }

    public static CharSequence getCommentText(String comment) {
        return comment.substring(comment.indexOf("$") + 1);
    }

    public static UIDisplayUtil getInstance() {
        return UIDisplayUtilWrapper.INSTANCE;
    }

    /**
     * Dp to pixel conversion.
     *
     * @param dp : dp to be converted
     * @return pixel : Converted value.
     */
    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * Common function to get Display metrics data
     *
     * @param context : context
     * @param status  : Request for particular data
     * @return integer : bases on status
     */
    public static int getDisplayMetricsData(Context context, int status) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        switch (status) {
            case GlobalVariable.DISPLAY_HEIGHT:
                return displayMetrics.heightPixels;
            case GlobalVariable.DISPLAY_WIDTH:
                return displayMetrics.widthPixels;
            default:
                return GlobalVariable.getInstance().getDisplayMetricsErrorState();
        }
    }

    public static void blurBitmapWithRenderscript(RenderScript rs, Bitmap bitmap2) {
        //this will blur the bitmapOriginal with a radius of 25 and save it in bitmapOriginal
        final Allocation input = Allocation.createFromBitmap(rs, bitmap2); //use this constructor for best performance, because it uses USAGE_SHARED mode which reuses memory
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        // must be >0 and <= 25
        script.setRadius(20f);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap2);
    }

    public static String getAudioPath(Uri uri) {
        String[] data = {MediaStore.Audio.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static String parseDateToddMMyyyy(Date time) {
        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(String.valueOf(time));
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static void saveSignInUserDetails(Context context, SignInModel body) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SIGN_UP_USER_DETAILS, MODE_PRIVATE).edit();
        editor.putString(context.getString(R.string.pref_object_id), body.getObjectId());
        editor.putString(context.getString(R.string.pref_display_name), body.getDisplayName());
        editor.putString(context.getString(R.string.pref_email_address), body.getEmailAddress());
        editor.putString(context.getString(R.string.pref_country), body.getCountry());
        editor.putString(context.getString(R.string.pref_city), body.getCity());
        editor.putString(context.getString(R.string.pref_identity_provider), body.getIdentityProvider());
        editor.putString(context.getString(R.string.pref_given_name), body.getGivenName());
        editor.putString(context.getString(R.string.pref_surname), body.getSurname());
        editor.apply();
    }

    public static SharedPreferences getSignupUserData(Context mContext) {
        return mContext.getSharedPreferences(SIGN_UP_USER_DETAILS, MODE_PRIVATE);
    }

    public static String getPathForVideo(Uri contentUri, Context mContext) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = mContext.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e("Video Uri", "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getPathForGalleryImageView(Uri uri, Context mContext) {
        String filePath = "";
        try {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = mContext.getContentResolver().query(uri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkPermission(final Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionSendMessage = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
            int readPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
            int writePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }
            if (readPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (writePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), AppConstants.REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkStoragePermission(final Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int readPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
            int writePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (readPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (writePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), AppConstants.REQUEST_ID_STORAGE_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    public static Bitmap captureView(View view, Context context) {
        Bitmap blurredBitmap = null;
        RenderScript renderScript = RenderScript.create(context);
        if (blurredBitmap != null) {
            return blurredBitmap;
        }
        blurredBitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(),
                Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(blurredBitmap);
        view.draw(canvas);
        UIDisplayUtil.blurBitmapWithRenderscript(renderScript, blurredBitmap);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000);    // darken
        paint.setColorFilter(filter);
        canvas.drawBitmap(blurredBitmap, 0, 0, paint);
        return blurredBitmap;
    }

    public static Bitmap loadBitmap(View backgroundView, View targetView, Context context) {
        Rect backgroundBounds = new Rect();
        backgroundView.getHitRect(backgroundBounds);
        if (!targetView.getLocalVisibleRect(backgroundBounds)) {
            return null;
        }
        Bitmap blurredBitmap = captureView(backgroundView, context);
        int[] location = new int[2];
        int[] backgroundViewLocation = new int[2];
        backgroundView.getLocationInWindow(backgroundViewLocation);
        targetView.getLocationInWindow(location);
        int height = targetView.getHeight();
        int y = location[1];
        if (backgroundViewLocation[1] >= location[1]) {
            height -= (backgroundViewLocation[1] - location[1]);
            if (y < 0)
                y = 0;
        }
        if (y + height > blurredBitmap.getHeight()) {
            height = blurredBitmap.getHeight() - y;
            if (height <= 0) {
                return null;
            }
        }
        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 0.5f);
        return Bitmap.createBitmap(blurredBitmap,
                (int) targetView.getX(),
                y,
                targetView.getMeasuredWidth(),
                height,
                matrix,
                true);
    }

    public static float getDp(Context context, int pixels) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, context.getResources().getDisplayMetrics());
    }

    public static void toggleZone(Context context, ToggleButton view) {
        if (view.isChecked()) {
            view.setBackground(context.getDrawable(R.drawable.unselected_text_view_bg));
            view.setElevation(0);
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            view.setTextColor(context.getColor(R.color.grey));
            view.setGravity(Gravity.CENTER);
        } else {
            view.setBackground(context.getDrawable(R.drawable.selected_textview_bg));
            view.setElevation(5);
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close_tag, 0);
            view.setTextColor(context.getColor(R.color.white));
            view.setGravity(Gravity.LEFT);
            view.setPadding(50,10,10,10);
        }
    }

    public static void setColor(View view, int color) {
        GradientDrawable bgShape = (GradientDrawable) view.getBackground();
        bgShape.setColor(color);
    }

    public void displaySnackBar(View currentView, String message) {
        Snackbar.make(currentView, message, Snackbar.LENGTH_LONG).show();
    }

    public void hideSoftKeyboard(View view, Context context) {
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void dismissPopupListWindow(ListPopupWindow listPopupWindow) {
        if (listPopupWindow != null && listPopupWindow.isShowing())
            listPopupWindow.dismiss();
    }

    private static class UIDisplayUtilWrapper {
        private static final UIDisplayUtil INSTANCE = new UIDisplayUtil();
    }

    /**
     * Call this method to enable "swipe down to close activity" function on any view (preferably the top one).<br/><br/>
     * <p><b>NOTE:</b> Use this method only when you don't have extra overrides in onBackPressed() method of the source activity.<br/><br/>
     * <b>Also, </b> {@code activity.onBackPressed()} was used because {@code activity.finish()} wasn't giving the desired transition animation.</p>
     *
     * @param activity The current activity.
     * @param view     The view on which swipe down gesture is required.
     */
    public static void setupSwipeGesture(Activity activity, View view) {
        activity.getWindow()
                .getSharedElementEnterTransition()
                .setDuration(activity.getResources().getInteger(R.integer.shared_element_animation_duration));
        activity.getWindow()
                .getSharedElementReturnTransition()
                .setDuration(activity.getResources().getInteger(R.integer.shared_element_animation_duration))
                .setInterpolator(new DecelerateInterpolator());
        view.setOnTouchListener(new OnSwipeTouchListener(activity) {
            @Override
            public void onSwipeDown() {
                activity.onBackPressed();
            }
        });
    }
}