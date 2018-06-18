package life.plank.juna.zone.util;

import android.content.Context;
import android.content.CursorLoader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.ListPopupWindow;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import life.plank.juna.zone.data.network.model.SignInModel;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by plank-sobia on 11/8/2017.
 */

public class UIDisplayUtil{

    private static String SIGN_UP_USER_DETAILS = "signUpPageDetails";

    private UIDisplayUtil(){

    }

    public static UIDisplayUtil getInstance(){
        return UIDisplayUtilWrapper.INSTANCE;
    }

    /**
     * Dp to pixel conversion.
     *
     * @param dp : dp to be converted
     * @return pixel : Converted value.
     */
    public static int dpToPx(int dp, Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round( dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT) );
    }

    /**
     * Common function to get Display metrics data
     *
     * @param context : context
     * @param status  : Request for particular data
     * @return integer : bases on status
     */
    public static int getDisplayMetricsData(Context context, int status){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService( Context
                .WINDOW_SERVICE );
        if(windowManager != null){
            windowManager.getDefaultDisplay().getMetrics( displayMetrics );
        }
        switch(status){
            case GlobalVariable.DISPLAY_HEIGHT:
                return displayMetrics.heightPixels;
            case GlobalVariable.DISPLAY_WIDTH:
                return displayMetrics.widthPixels;
            default:
                return GlobalVariable.getInstance().getDisplayMetricsErrorState();
        }
    }

    public static void blurBitmapWithRenderscript(RenderScript rs, Bitmap bitmap2){
        //this will blur the bitmapOriginal with a radius of 25 and save it in bitmapOriginal
        final Allocation input = Allocation.createFromBitmap( rs, bitmap2 ); //use this
        // constructor for best performance, because it uses USAGE_SHARED mode which reuses memory
        final Allocation output = Allocation.createTyped( rs, input.getType() );
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create( rs, Element.U8_4( rs ) );
        // must be >0 and <= 25
        script.setRadius( 10f );
        script.setInput( input );
        script.forEach( output );
        output.copyTo( bitmap2 );
    }

    public static String getAudioPath(Uri uri){
        String[] data = {MediaStore.Audio.Media.DATA};
        CursorLoader loader = new CursorLoader( getApplicationContext(), uri, data, null, null,
                null );
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow( MediaStore.Audio.Media.DATA );
        cursor.moveToFirst();
        return cursor.getString( column_index );
    }

    public static String parseDateToddMMyyyy(Date time){
        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat( inputPattern );
        SimpleDateFormat outputFormat = new SimpleDateFormat( outputPattern );
        Date date = null;
        String str = null;
        try{
            date = inputFormat.parse( String.valueOf( time ) );
            str = outputFormat.format( date );
        }catch(ParseException e){
            e.printStackTrace();
        }
        return str;
    }

    public static void saveSignInUserDetails(Context mContext, SignInModel body){
        SharedPreferences.Editor editor = mContext.getSharedPreferences( SIGN_UP_USER_DETAILS, MODE_PRIVATE ).edit();
        editor.putString( "objectId", body.getObjectId() );
        editor.putString( "displayName", body.getDisplayName() );
        editor.putString( "emailAddress", body.getEmailAddress() );
        editor.putString( "country", body.getCountry() );
        editor.putString( "city", body.getCity() );
        editor.putString( "identityProvider", body.getIdentityProvider() );
        editor.putString( "givenName", body.getGivenName() );
        editor.putString( "surname", body.getSurname() );
        editor.apply();
    }

    public static SharedPreferences getSignupUserData(Context mContext){
        return mContext.getSharedPreferences( SIGN_UP_USER_DETAILS, MODE_PRIVATE );
    }

    public static String getPathForVideo(Uri contentUri, Context mContext){
        Cursor cursor = null;
        try{
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = mContext.getContentResolver().query( contentUri, proj, null, null, null );
            int column_index = cursor.getColumnIndexOrThrow( MediaStore.Images.Media.DATA );
            cursor.moveToFirst();
            return cursor.getString( column_index );
        }catch(Exception e){
            Log.e( "Video Uri", "getRealPathFromURI Exception : " + e.toString() );
            return "";
        }finally{
            if(cursor != null){
                cursor.close();
            }
        }
    }

    public static String getPathForGalleryImageView(Uri uri, Context mContext){
        String filePath = "";
        try{
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = mContext.getContentResolver().query( uri, filePathColumn, null, null, null );
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex( filePathColumn[0] );
            filePath = cursor.getString( columnIndex );
            cursor.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return filePath;
    }

    public void displaySnackBar(View currentView, String message){
        Snackbar.make( currentView, message, Snackbar.LENGTH_LONG ).show();
    }

    public void hideSoftKeyboard(View view, Context context){
        if(view != null){
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService
                    ( Context.INPUT_METHOD_SERVICE );
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow( view.getWindowToken(), 0 );
        }
    }

    public void dismissPopupListWindow(ListPopupWindow listPopupWindow){
        if(listPopupWindow != null && listPopupWindow.isShowing())
            listPopupWindow.dismiss();
    }

    private static class UIDisplayUtilWrapper{
        private static final UIDisplayUtil INSTANCE = new UIDisplayUtil();
    }
}
