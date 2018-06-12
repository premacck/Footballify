package life.plank.juna.zone.firebasepushnotification.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getSimpleName();
    private static final String TABLE_UPLOADS = "uploadImage";
    private static final String KEY_ID = "id";
    private static final String KEY_DATA = "uploadContentList";
    private Context applicationContext;


    public DBHelper(Context applicationContext) {
        super( applicationContext, "upload.db", null, 1 );
        this.applicationContext = applicationContext;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query;
        query = "CREATE TABLE " + TABLE_UPLOADS + " (" + KEY_ID + " INTEGER PRIMARY KEY autoincrement," + KEY_DATA + " TEXT)";
        sqLiteDatabase.execSQL( query );
        Log.e( TAG, "uploadImage Table Created" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query;
        query = "DROP TABLE IF EXISTS " + TABLE_UPLOADS;
        sqLiteDatabase.execSQL( query );
        onCreate( sqLiteDatabase );
    }

    public void insertNotificationDataInDatabase( String list) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( KEY_DATA, list );
        Cursor res = database.rawQuery( "select * from " + TABLE_UPLOADS, null );
        res.moveToFirst();
        long rowInserted = database.insert( TABLE_UPLOADS, null, values );
        if (rowInserted != -1)
            Log.e( TAG, "INSERTED at row " + rowInserted );
        database.close();
    }

    public ArrayList<String> getDataList() {
        ArrayList<String> dataList = new ArrayList<>();
        dataList.clear();
        String selectQuery = "SELECT  * FROM " + TABLE_UPLOADS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( selectQuery, null );
        String data = null;

        if (cursor.moveToFirst()) {
            do {
                // get the data into array, or class variable
                data = cursor.getString( 1 );
                dataList.add( data );
            } while (cursor.moveToNext());
        }
        Log.e( TAG, "Get DB List" + dataList );
        cursor.close();
        return  dataList;
    }

    // delete table on logout
    public void deleteTable() {
        Log.e( TAG, "delete" );
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_UPLOADS;
        Log.e( "query", deleteQuery );
        database.execSQL( deleteQuery );
    }
/*
    public void updateDataList(String list, String contentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CLASS_NAME, subjectNewName);
        db.update(TABLE_CLASSES, values, KEY_CLASS_NAME + " = ?", new String[]{subjectOldName});
        db.update(TABLE_STUDENTS, values, KEY_CLASS_NAME + " = ?", new String[]{subjectOldName});
    }*/

    // Deleting single contact
  /*  public void deleteDataList(int contentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deleted = db.delete( TABLE_UPLOADS, KEY_CONTENT_ID + " = ?", new String[]{String.valueOf( contentId )} );
        Log.e( TAG, "deleted " + deleted );
        db.close();
    }*/
}
