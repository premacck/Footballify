package life.plank.juna.zone.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.database.model.SavePushNotification;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "pushNotificationManager";
    private static final String TABLE_PUSH_NOTIFICATION = "pushNotification";
    private static final String KEY_HEADER = "header";
    private static final String KEY_BODY = "body";
    private static final String KEY_DATE = "date";
    private static final String KEY_ID = "id";

    public DatabaseHandler(Context context) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PUSH_NOTIFICATION_TABLE = "CREATE TABLE " + TABLE_PUSH_NOTIFICATION + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"+ KEY_HEADER + " TEXT, "+ KEY_BODY + " TEXT,"
                + KEY_DATE + " TEXT" + ")";
        db.execSQL( CREATE_PUSH_NOTIFICATION_TABLE );
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_PUSH_NOTIFICATION );
        onCreate( db );
    }

    public void addPushNotificationData(SavePushNotification savePushNotification) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( KEY_HEADER, savePushNotification.getHeader() );
        values.put( KEY_BODY, savePushNotification.getBody() );
        values.put( KEY_DATE, savePushNotification.getDate() );
        // Inserting Row
        db.insert( TABLE_PUSH_NOTIFICATION, null, values );
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public List<SavePushNotification> getAllPushNotification() {
        List<SavePushNotification> savePushNotificationList = new ArrayList<SavePushNotification>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PUSH_NOTIFICATION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( selectQuery, null );

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SavePushNotification savePushNotification = new SavePushNotification();
                savePushNotification.setHeader( cursor.getString( 1 ) );
                savePushNotification.setBody( cursor.getString( 2 ) );
                savePushNotification.setDate( cursor.getString( 3 ) );
                // Adding contact to list
                savePushNotificationList.add( savePushNotification );
            } while (cursor.moveToNext());
        }

        return savePushNotificationList;
    }
}
