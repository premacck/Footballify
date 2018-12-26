package life.plank.juna.zone.util.view;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.renderscript.Allocation;
import androidx.renderscript.Element;
import androidx.renderscript.RenderScript;
import androidx.renderscript.ScriptIntrinsicBlur;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.Emoji;
import life.plank.juna.zone.data.model.FeedEntry;
import life.plank.juna.zone.util.common.AppConstants;
import life.plank.juna.zone.util.customview.TopGravityDrawable;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static android.text.style.DynamicDrawableSpan.ALIGN_BASELINE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.prembros.facilis.util.DataUtilKt.isNullOrEmpty;
import static life.plank.juna.zone.util.common.AppConstants.SPACE;

/**
 * Created by plank-sobia on 11/8/2017.
 */

public class UIDisplayUtil {

    public static final StyleSpan BOLD_STYLE = new StyleSpan(Typeface.createFromAsset(ZoneApplication.getContext().getAssets(), "rajdhani_bold.ttf").getStyle());
    public static final Typeface SEMI_BOLD_TYPEFACE = Typeface.createFromAsset(ZoneApplication.getContext().getAssets(), "fonts/rajdhani_semibold.ttf");
    public static final StyleSpan SEMI_BOLD_STYLE = new StyleSpan(SEMI_BOLD_TYPEFACE.getStyle());

    //TODO: Remove once implemented on backend
    public static ArrayList<Emoji> emoji = new ArrayList<>();

    static {
        addDefaultEmojis(emoji);
    }

    public static void addDefaultEmojis(List<Emoji> emojiList) {
        emojiList.add(new Emoji(0x1F609, 0));
        emojiList.add(new Emoji(0x1F620, 0));
        emojiList.add(new Emoji(0x1F922, 0));
        emojiList.add(new Emoji(0x1F60D, 0));
        emojiList.add(new Emoji(0x1F92F, 0));
        emojiList.add(new Emoji(0x1F604, 0));
        emojiList.add(new Emoji(0x1F610, 0));
        emojiList.add(new Emoji(0x1F910, 0));
        emojiList.add(new Emoji(0x1F917, 0));
        emojiList.add(new Emoji(0x1F92E, 0));
        emojiList.add(new Emoji(0x1F924, 0));
        emojiList.add(new Emoji(0x1F92B, 0));
    }

    @Nullable
    public static Drawable getCommentColor(String comment) {
        if (isNullOrEmpty(comment)) return null;
        return DrawableHashMap.getDrawableMap().get(comment.substring(0, comment.indexOf("$")));
    }

    @Nullable
    public static CharSequence getCommentText(String comment) {
        if (isNullOrEmpty(comment)) return null;
        return comment.substring(comment.indexOf("$") + 1);
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
        int verticalPadding = (int) getDp(2);
        if (!isChecked) {
            view.setBackground(context.getDrawable(R.drawable.unselected_text_view_bg));
            view.setElevation(verticalPadding);
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            view.setTextColor(context.getColor(R.color.grey));
            view.setGravity(Gravity.CENTER);
            view.setPadding(0, verticalPadding, 0, verticalPadding);
        } else {
            view.setBackground(context.getDrawable(R.drawable.selected_textview_bg));
            view.setElevation(getDp(5));
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close_tag, 0);
            view.setTextColor(context.getColor(R.color.white));
            view.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            view.setPadding((int) getDp(13), verticalPadding, (int) getDp(6), verticalPadding);
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

    public static SimpleTarget<Drawable> getStartDrawableTarget(TextView textView) {
        return new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                textView.setCompoundDrawablesWithIntrinsicBounds(resource, null, null, null);
            }
        };
    }

    public static SimpleTarget<Drawable> getEndDrawableTarget(TextView textView) {
        return new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, resource, null);
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
                                                           @DrawableRes int startDrawableRes, boolean toUpperCase) {
        if (isNullOrEmpty(boldText)) {
            return new SpannableStringBuilder(normalText);
        } else {
            if (toUpperCase) {
                normalText = normalText.replace(boldText, boldText.toUpperCase());
                boldText = boldText.toUpperCase();
            }
            SpannableStringBuilder text = new SpannableStringBuilder(normalText);
            int startIndex = text.toString().indexOf(boldText);
            text.setSpan(BOLD_STYLE, startIndex, startIndex + boldText.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
            if (color != -1) {
                text.setSpan(
                        new ForegroundColorSpan(ResourcesCompat.getColor(ZoneApplication.getContext().getResources(), color, null)),
                        startIndex, startIndex + boldText.length(), SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
            if (startDrawableRes != -1) {
                text.insert(0, SPACE);
                text.setSpan(
                        new ImageSpan(ZoneApplication.getContext(), startDrawableRes, ALIGN_BASELINE),
                        0, 1, SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
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

    public static void showSoftKeyboard(View view) {
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) ZoneApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
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

    public static int[] getScreenSize(@Nullable Activity activity) {
        if (activity == null) return new int[] {0, 0};
        return getScreenSize(activity.getWindowManager().getDefaultDisplay());
    }

    public static int[] getScreenSize(Display display) {
        Point size = new Point();
        display.getSize(size);
        return new int[] {size.x, size.y};
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

    public static int findColor(@ColorRes int color) {
        return ZoneApplication.getContext().getColor(color);
    }

    public static Drawable findDrawable(@DrawableRes int drawable) {
        return ZoneApplication.getContext().getResources().getDrawable(drawable, null);
    }

    public static int getMasonryLayoutCellSpan(int position) {
        return position % 18 == 0 || position % 18 == 10 ? 2 : 1;
    }

    public static void setupFeedEntryByMasonryLayout(List<FeedEntry> feedEntryList) {
        for (FeedEntry feedEntry : feedEntryList) {
            int position = feedEntryList.indexOf(feedEntry);
            feedEntry.setCSpan(getMasonryLayoutCellSpan(position));
            feedEntry.setRSpan(feedEntry.getCSpan());
        }
    }

    public void dismissPopupListWindow(ListPopupWindow listPopupWindow) {
        if (listPopupWindow != null && listPopupWindow.isShowing())
            listPopupWindow.dismiss();
    }
}