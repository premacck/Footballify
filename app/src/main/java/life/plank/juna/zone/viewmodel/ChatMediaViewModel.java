package life.plank.juna.zone.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

import life.plank.juna.zone.data.network.model.ChatMediaViewData;
import life.plank.juna.zone.util.AppConstants;

/**
 * Created by plank-niraj on 07-03-2018.
 */

public class ChatMediaViewModel {
    private Context context;
    private Cursor cursor;

    public ChatMediaViewModel(Context context) {
        this.context = context;
    }

    public void getAllMedia(ArrayList<ChatMediaViewData> mediaData) {
        getDataFromProvider();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            boolean isImage = false;
            if (cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE)) == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                isImage = true;
            }
            ChatMediaViewData chatMediaViewData = new ChatMediaViewData(cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)), isImage, false);
            chatMediaViewData.setMediaType(AppConstants.CHAT_MEDIA_MEDIA_TYPE);
            mediaData.add(chatMediaViewData);
        }
        cursor.close();
    }

    private void getDataFromProvider() {
        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.TITLE
        };
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + AppConstants.CHAT_MEDIA_EQUAL
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + AppConstants.CHAT_MEDIA_OR
                + MediaStore.Files.FileColumns.MEDIA_TYPE + AppConstants.CHAT_MEDIA_EQUAL
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
        Uri queryUri = MediaStore.Files.getContentUri(AppConstants.CHAT_MEDIA_EXTERNAL);
        cursor = context.getContentResolver().query(queryUri, projection, selection, null, MediaStore.Files.FileColumns.DATE_ADDED);
    }
}