package life.plank.juna.zone.util;


import android.text.format.DateFormat;

import java.text.SimpleDateFormat;

/**
 * Created by plank-hasan on 2/9/2018.
 */
public class AppConstants {
    public static final String FOOTBALL_FEEDS_HEADER_KEY = "newsfeed-continuation-token";
    public static final String DEFAULT_ERROR_MESSAGE = "Something went wrong, Please try again later!";
    public static final String PINNED_FEEDS = "pinnedfeeds";
    public static final int DELAY = 1200;
    public static final int PAGINATION_DELAY = 1000;
    public static final int SPINNER_DIALOG_WIDTH = 150;
    public static final String POSITION = "position";
    public static final String CHAT_DETAILS_IMAGE = "chatdetailsimage";
    public static final String DEFAULT_IMAGE_URL = "http://avasportcentral.com/wp-content/themes/micron/images/placeholders/placeholder_large_dark.jpg";
    public static final String REGULAR_EXPRESSION_RT = "RT:(\\d*)";
    public static final String REGULAR_EXPRESSION_TRC = "TRC:(\\d*)";
    public static final String CHAT_MEDIA_OR = " OR ";
    public static final String CHAT_MEDIA_EQUAL = " = ";
    public static final String CHAT_MEDIA_EXTERNAL = "external";
    public static final int CHAT_MEDIA_MEDIA_TYPE = 1;
    public static final int CHAT_MEDIA_GIF_TYPE = 3;
    public static final int CHAT_MEDIA_STICKER_TYPE = 2;
    public static final int REQUEST_CAMERA_STORAGE = 1001;
    public static final int REQUEST_GALLERY = 1002;
    public static final int REQUEST_VIDEO_CAPTURE = 1111;
    public static final int REQUEST_AUDIO_RECORDER = 2222;
    public static final String RECORDED_AUDIO = "recordedAudio";
    public static final String SELECTED_GALLERY_IMAGE = "selectedGalleryImage";
    public static final String DEFAULT_RECORDED_AUDIO_IMAGE = "https://lh3.ggpht.com/DUPr-ZmdjIwT1x-mq8tESQehLM_hLocsl2QYvaV5_5lMfYsf56WxMI7FvaOd-KBMWDs=h300";
    public static final String FEED_ITEMS = "feeditems";
    public static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    public static final String CAPTURED_IMAGE_NAME = "Zone_";
    public static final String CAPTURED_IMAGES_FOLDER_NAME = "Zone";
    public static final String CAPTURED_IMAGES_SUB_FOLDER_NAME = "Images";
    public static final String CAPTURED_IMAGE_FORMAT = ".png";
    public static final String FILE_PROVIDER_TO_CAPTURE_IMAGE = "life.plank.juna.zone.fileprovider";
    public static final int CAMERA_IMAGE_RESULT = 5;
    public static final int AUDIO_PICKER_RESULT = 8;
    public static final int GALLERY_IMAGE_RESULT = 7;
    public static final int VIDEO_CAPTURE = 101;
    public static final String CACHE_FILE_NAME = "feeditems.txt";
    public static final String DATE_FORMAT_FEED_DETAILS = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String DAY_FORMAT = "dd";
    public static final String MONTH_FORMAT = "MMM";
    public static final String YEAR_FORMAT = "yyyy";
    public static final String TIME_FORMAT = "h:mm a";
    public static final String LEAGUE_NAME = "Premier League";
    //todo : Retrieve this from the api. will do in next pull request
    public static final String SEASON_NAME = "2017/2018";
    public static final int ANIMATION_DURATION = 200;
    public static final float ANIMATION_START_SCALE = 1.0f;
    public static final float ANIMATION_END_SCALE = 1.08f;
    public static final float ANIMATION_PIVOT_VALUE = 0.5f;
    public static final int REQUEST_CAMERA_PERMISSION = 1002;
    public static final int REQUEST_STORAGE_PERMISSION = 1;
    public static final String AUDIO_PATH = "audio_path";
    public static final String BOARD_TYPE = "FootballMatch";


    public static String getDateAndTime(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppConstants.DATE_FORMAT_FEED_DETAILS);
        try {
            return DateFormat.format(AppConstants.DAY_FORMAT, simpleDateFormat.parse(date))
                    + " "
                    + DateFormat.format(AppConstants.MONTH_FORMAT, simpleDateFormat.parse(date))
                    + " "
                    + DateFormat.format(AppConstants.YEAR_FORMAT, simpleDateFormat.parse(date))
                    + " | "
                    + DateFormat.format(AppConstants.TIME_FORMAT, simpleDateFormat.parse(date));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}