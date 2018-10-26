package life.plank.juna.zone.util;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.ListPopupWindow;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.Emoji;
import life.plank.juna.zone.data.model.User;
import life.plank.juna.zone.util.customview.TopGravityDrawable;
import life.plank.juna.zone.view.activity.web.WebCardActivity;

import static android.content.Context.MODE_PRIVATE;
import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;

/**
 * Created by plank-sobia on 11/8/2017.
 */

public class UIDisplayUtil {

    private static final StyleSpan BOLD_STYLE = new StyleSpan(Typeface.createFromAsset(ZoneApplication.getContext().getAssets(), "rajdhani_bold.ttf").getStyle());

    //TODO: Remove once implemented on backend
    public static Emoji[] emoji = new Emoji[]{
            new Emoji("winking", R.drawable.ic_emoji_winking, 0x1F609, 324),
            new Emoji("angry", R.drawable.ic_emoji_angry, 0x1F620, 765),
            new Emoji("nauseated", R.drawable.ic_emoji_nauseated, 0x1F922, 987),
            new Emoji("heart", R.drawable.ic_emoji_heart_eyes, 0x1F60D, 124),
            new Emoji("exploding", R.drawable.ic_emoji_exploding_head, 0x1F92F, 878),
            //TODO: uncomment after the emoji bottom sheet design is finalised
//            new Emoji("happy", R.drawable.ic_emoji_happy, 0x1F604),
//            new Emoji("neutral", R.drawable.ic_emoji_neutral, 0x1F610),
//            new Emoji("zipped", R.drawable.ic_emoji_zipped_mouth, 0x1F910),
//            new Emoji("hugging", R.drawable.ic_emoji_hugging, 0x1F917),
//            new Emoji("vomiting", R.drawable.ic_emoji_vomiting, 0x1F92E),
//            new Emoji("drool", R.drawable.ic_emoji_drool, 0x1F924),
//            new Emoji("shush", R.drawable.ic_emoji_shush, 0x1F92B),
    };

    public UIDisplayUtil() {

    }

    public static Drawable getCommentColor(String comment) {
        DrawableHashMap.HashMaps(getApplicationContext());
        int color = DrawableHashMap.getDrawableMap().get(comment.substring(0, comment.indexOf("$")));
        return getApplicationContext().getResources().getDrawable(color, null);
    }

    public static CharSequence getCommentText(String comment) {
        return comment.substring(comment.indexOf("$") + 1);
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
        script.setRadius(25f);
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

    public static void saveSignInUserDetails(Context context, User body) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.pref_user_details), MODE_PRIVATE).edit();
        editor.putString(context.getString(R.string.pref_object_id), body.getObjectId());
        editor.putString(context.getString(R.string.pref_display_name), body.getDisplayName());
        editor.putString(context.getString(R.string.pref_email_address), body.getEmailAddress());
        editor.putString(context.getString(R.string.pref_country), body.getCountry());
        editor.putString(context.getString(R.string.pref_city), body.getCity());
        editor.putString(context.getString(R.string.pref_profile_pic_url), body.getProfilePictureUrl());
        editor.apply();
    }

    public static SharedPreferences getSignupUserData(Context context) {
        return context.getSharedPreferences(context.getString(R.string.pref_user_details), MODE_PRIVATE);
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

    public static Bitmap captureView(View view, Context context) {
        Bitmap blurredBitmap;
        RenderScript renderScript = RenderScript.create(context);
        blurredBitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(),
                Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(blurredBitmap);
        view.draw(canvas);
        blurBitmapWithRenderscript(renderScript, blurredBitmap);
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

    public static float getDp(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ZoneApplication.getContext().getResources().getDisplayMetrics());
    }

    public static void toggleZone(Context context, ToggleButton view, boolean isChecked) {
        view.setChecked(!isChecked);
        if (!isChecked) {
            view.setBackground(context.getDrawable(R.drawable.unselected_text_view_bg));
            view.setElevation(getDp(2));
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            view.setTextColor(context.getColor(R.color.grey));
            view.setGravity(Gravity.CENTER);
        } else {
            view.setBackground(context.getDrawable(R.drawable.selected_textview_bg));
            view.setElevation(getDp(5));
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close_tag, 0);
            view.setTextColor(context.getColor(R.color.white));
            view.setGravity(Gravity.START);
            view.setPadding(50, 10, 10, 10);
        }
    }

    public static void setColor(View view, int color) {
        GradientDrawable bgShape = (GradientDrawable) view.getBackground();
        bgShape.setColor(color);
    }

    public static Bitmap getScreenshot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] byteArray = stream.toByteArray();
        try {
            stream.close();
        } catch (Exception e) {
            Log.e("getScreenshot", e.getMessage());
        }
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    /**
     * Call this method to enable "swipe down to close activity" function on any view (preferably the top one).<br/><br/>
     * <p><b>NOTE:</b> Use this method only when you don't have extra overrides in onBackPressed() method of the source activity.<br/><br/>
     * <b>Also, </b> {@code activity.onBackPressed()} was used because {@code activity.finish()} wasn't giving the desired transition animation.</p>
     *
     * @param activity The current activity.
     * @param dragView The view on which swipe down gesture is required.
     * @param rootView The root view of the layout, which is supposed to move along with the swipe.
     */
    public static void setupSwipeGesture(Activity activity, View dragView, View rootView) {
        dragView.setOnTouchListener(new OnSwipeTouchListener(activity, dragView, rootView) {
            @Override
            public void onSwipeDown() {
                activity.onBackPressed();
            }
        });
    }

    public static void setupSwipeGesture(Activity activity, View dragView, View rootView, ViewGroup backgroundLayout) {
        dragView.setOnTouchListener(new OnSwipeTouchListener(activity, dragView, rootView, backgroundLayout) {
            @Override
            public void onSwipeDown() {
                activity.finish();
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    public static void setSharedElementTransitionDuration(Activity activity, int duration) {
        activity.getWindow().getSharedElementEnterTransition().setDuration(duration);
        activity.getWindow().getSharedElementReturnTransition().setDuration(duration).setInterpolator(new DecelerateInterpolator());
    }

    public static Target getStartDrawableTarget(TextView textView) {
        return new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                textView.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(ZoneApplication.getContext().getResources(), bitmap), null, null, null);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
    }

    public static Target getEndDrawableTarget(TextView textView) {
        return new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, new BitmapDrawable(ZoneApplication.getContext().getResources(), bitmap), null);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
    }

    public static SimpleTarget<Drawable> getDrawableTopTarget(TextView textView) {
        return new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                textView.setCompoundDrawablesWithIntrinsicBounds(null, resource, null, null);
            }
        };
    }

    public static void alternateBackgroundColor(View view, int position) {
        view.setBackgroundColor(ZoneApplication.getContext().getColor(
                position % 2 == 0 ?
                        R.color.white :
                        R.color.background_color
        ));
    }

    public static Drawable getTopGravityDrawable(@DrawableRes int drawableRes) {
        if (drawableRes == -1) {
            return null;
        } else {
            Drawable drawable = ZoneApplication.getContext().getResources().getDrawable(drawableRes, null);
            TopGravityDrawable topGravityDrawable = new TopGravityDrawable(drawable);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            topGravityDrawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            return topGravityDrawable;
        }
    }

    /**
     * @param boldText   The text part you want to make bold.
     * @param normalText The {@link SpannableStringBuilder} containing the whole  string which also contains the bold text part.
     * @param color      The color of the bold text.
     * @return String with required Bold text replaced with the normal text.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static SpannableStringBuilder getDesignedString(String boldText, String normalText, @ColorRes int color,
                                                           @DrawableRes int startDrawableRes, boolean toUpperCase, TextView commentaryView) {
        if (isNullOrEmpty(boldText)) {
            commentaryView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            return new SpannableStringBuilder(normalText);
        } else {
            commentaryView.setCompoundDrawablesWithIntrinsicBounds(getTopGravityDrawable(startDrawableRes), null, null, null);
            if (toUpperCase) {
                normalText = normalText.replace(boldText, boldText.toUpperCase());
                boldText = boldText.toUpperCase();
            }
            SpannableStringBuilder text = new SpannableStringBuilder(normalText);
            int startIndex = text.toString().indexOf(boldText);
            text.setSpan(BOLD_STYLE, startIndex, startIndex + boldText.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setSpan(
                    new ForegroundColorSpan(ResourcesCompat.getColor(ZoneApplication.getContext().getResources(), color, null)),
                    startIndex, startIndex + boldText.length(), SPAN_EXCLUSIVE_EXCLUSIVE
            );
            return text;
        }
    }

    public static CharSequence getSpannedString(@StringRes int stringRes) {
        try {
            if (stringRes == R.string.board_yet_to_be_populated) {
                SpannableStringBuilder builder = new SpannableStringBuilder(ZoneApplication.getContext().getString(stringRes));
                builder.setSpan(BOLD_STYLE, 31, 56, SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(BOLD_STYLE, 97, 112, SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(
                        new ForegroundColorSpan(ResourcesCompat.getColor(ZoneApplication.getContext().getResources(), R.color.green, null)),
                        31, 56, SPAN_EXCLUSIVE_EXCLUSIVE
                );
                builder.setSpan(
                        new ForegroundColorSpan(ResourcesCompat.getColor(ZoneApplication.getContext().getResources(), R.color.text_hint_label_color, null)),
                        97, 112, SPAN_EXCLUSIVE_EXCLUSIVE
                );
                return builder;
            }
        } catch (Exception e) {
            Log.e("getSpannedString()", e.getMessage());
        }
        return ZoneApplication.getContext().getString(stringRes);
    }

    public static void displaySnackBar(View currentView, @StringRes int message) {
        Snackbar.make(currentView, message, Snackbar.LENGTH_LONG).show();
    }

    public static void hideSoftKeyboard(View view) {
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) ZoneApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void enableOrDisableView(View view, boolean isEnabled, boolean... isTintRequired) {
        view.setEnabled(isEnabled);
        view.setClickable(isEnabled);
        view.setAlpha(isEnabled ? 1f : 0.5f);
        if (isTintRequired.length > 0 && isTintRequired[0]) {
            view.setBackgroundTintList(isEnabled ? null : ColorStateList.valueOf(ZoneApplication.getContext().getColor(R.color.colorDisabled)));
        }
    }

    public static void showBoardExpirationDialog(Activity activity, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(activity)
                .setTitle(R.string.alert)
                .setMessage(R.string.board_now_expired)
                .setPositiveButton(R.string.okay, listener)
                .show();
    }

    public static int[] getScreenSize(Display display) {
        Point size = new Point();
        display.getSize(size);
        return new int[]{size.x, size.y};
    }

    public static SpannableString getBoldText(String text) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(BOLD_STYLE, 0, text.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableString getClickableLink(Activity activity, String url) {
        SpannableString spannableString = new SpannableString(url);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                WebCardActivity.launch(activity, url);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        }, 0, url.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(
                new ForegroundColorSpan(ResourcesCompat.getColor(ZoneApplication.getContext().getResources(), R.color.dark_sky_blue, null)),
                0, url.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    public static void flipView(View viewToFlip, boolean isVertical) {
        ObjectAnimator flip = ObjectAnimator.ofFloat(viewToFlip, ZoneApplication.getContext().getString(isVertical ? R.string.rotation_y : R.string.rotation_x), 0f, 180f);
        flip.setDuration(500);
        flip.start();
    }

    public static void rotateAndChangeImageResource(final ImageView imageView, @DrawableRes final int resId) {
        imageView.setRotation(0);
        imageView.animate()
                .rotationBy(360)
                .setDuration(400)
                .setInterpolator(new OvershootInterpolator())
                .start();

        imageView.postDelayed(() -> imageView.setImageResource(resId), 120);
    }

    public static void touchFeedBackAnimation(@NotNull View view, boolean isDown) {
        view.animate()
                .scaleX(isDown ? 0.88f : 1f)
                .scaleY(isDown ? 0.88f : 1f)
                .setDuration(300)
                .setInterpolator(new OvershootInterpolator())
                .start();
    }

    public void dismissPopupListWindow(ListPopupWindow listPopupWindow) {
        if (listPopupWindow != null && listPopupWindow.isShowing())
            listPopupWindow.dismiss();
    }

    public static int getColor(@ColorRes int color) {
        return ZoneApplication.getContext().getColor(color);
    }
}