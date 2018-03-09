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
        try {
            getDataFromProvider();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                boolean isImage = false;
                if (cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE)) == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                    isImage = true;
                }
                ChatMediaViewData chatMediaViewData = new ChatMediaViewData(cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)), false, isImage);
                chatMediaViewData.setMediaType(AppConstants.CHAT_MEDIA_MEDIA_TYPE);
                mediaData.add(chatMediaViewData);
            }
        } finally {
            cursor.close();
        }
    }

    private void getDataFromProvider() {
        // TODO: 07-03-2018 write integrate tests for provider
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

    public void addStickersData(ArrayList<ChatMediaViewData> mediaData) {
        // TODO: 01-03-2018 Replace with server data and remove hardcoded data
        mediaData.clear();
        ChatMediaViewData chatMediaViewModel = new ChatMediaViewData("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRviCJBVIDqcNsyGTZB8zRKFUb0opxp3ffNh2ExbRe0jOfARi5_", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_STICKER_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSx3ZyX-KbqMKeI2xLB_IVAPnbYWKM1aeZ72W6acASzlcShNgq0oQ", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_STICKER_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://sdl-stickershop.line.naver.jp/products/0/0/1/1302829/android/stickers/12243284.png", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_STICKER_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://stickershop.line-scdn.net/stickershop/v1/product/1278190/LINEStorePC/main@2x.png;compress=true", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_STICKER_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://n9neo.files.wordpress.com/2013/09/real-madrid-goal.png", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_STICKER_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://lh3.googleusercontent.com/edk61NIBjCZ1Fm5VFb2y2b6BMdkwH9QSMOIzbx5TTYqVgwhM4KODM_9zNpMcM9NneSsUZOr_chSAPZDX_5W2Uc1zJqmMXhnxy5Glu-cjaF9Lo11GeBa6u8Z_", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_STICKER_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://ih1.redbubble.net/image.420209542.6303/sticker,375x360-bg,ffffff.u9.png", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_STICKER_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT_eDWkAczray8NDaq_jLXKF85jrS6v8_pKRfqRgU3GFHZ6iArJ_A", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_STICKER_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSL3L59QiP55GKnJcwMElHHKDCVf69KDZqNMkxKH9PQHp-NPqZ5ZQ", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_STICKER_TYPE);
        mediaData.add(chatMediaViewModel);
    }

    public void addGifData(ArrayList<ChatMediaViewData> mediaData) {
        // TODO: 01-03-2018 Replace with server data and remove hardcoded data
        mediaData.clear();
        ChatMediaViewData chatMediaViewModel = new ChatMediaViewData("https://thumbs.gfycat.com/GrimSimplisticGermanspaniel-max-1mb.gif", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_GIF_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://media.giphy.com/media/Cs4yoF8IGx25G/giphy.gif", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_GIF_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://media.giphy.com/media/RbWR9d1tJ1dok/giphy.gif", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_GIF_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("http://i49.tinypic.com/kdx26b.jpg", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_GIF_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://media.giphy.com/media/PbfN8XKjFBpaE/giphy.gif", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_GIF_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://media.giphy.com/media/T0lTQ0LX3FvpK/giphy.gif", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_GIF_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://i.imgur.com/sYqnosD.gif", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_GIF_TYPE);
        mediaData.add(chatMediaViewModel);

        chatMediaViewModel = new ChatMediaViewData("https://thumbs.gfycat.com/ObedientHappyDore-max-1mb.gif", false, false);
        chatMediaViewModel.setMediaType(AppConstants.CHAT_MEDIA_GIF_TYPE);
        mediaData.add(chatMediaViewModel);
    }
}