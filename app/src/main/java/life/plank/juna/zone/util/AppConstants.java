package life.plank.juna.zone.util;


/**
 * Created by plank-hasan on 2/9/2018.
 */
public class AppConstants {
    public static final String FOOTBALL_FEEDS_HEADER_KEY = "newsfeed-continuation-token";
    public static final String PINNED_FEEDS = "pinnedfeeds";
    public static final int PAGINATION_DELAY = 1000;

    public static final String REGULAR_EXPRESSION_RT = "RT:(\\d*)";
    public static final String REGULAR_EXPRESSION_TRC = "TRC:(\\d*)";
    public static final String RECORDED_AUDIO = "recordedAudio";

    public static final String FILE_PROVIDER_TO_CAPTURE_IMAGE = "life.plank.juna.zone.fileprovider";
    public static final int CAMERA_IMAGE_RESULT = 5;
    public static final int AUDIO_PICKER_RESULT = 8;
    public static final int GALLERY_IMAGE_RESULT = 7;
    public static final int VIDEO_CAPTURE = 101;
    public static final String CACHE_FILE_NAME = "feeditems.txt";
    public static final String LEAGUE_NAME = "Premier League";
    //todo : Retrieve this from the api. will do in next pull request
    public static final String SEASON_NAME = "2018/2019";
    public static final String COUNTRY_NAME = "England";
    public static final int ANIMATION_DURATION = 200;
    public static final float ANIMATION_START_SCALE = 1.0f;
    public static final float ANIMATION_PIVOT_VALUE = 0.5f;

    public static final String BOARD_TYPE = "FootballMatch";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    public static final int REQUEST_ID_STORAGE_PERMISSIONS = 102;
    public static final String SHARE_TO = "Board";
    public static final String ROOT_COMMENT = "rootComment";

    public static final String LOAD_VIEW = "loadView";
    public static final String STANDINGS = "standings";
    public static final String TEAM_STATS = "teamStats";
    public static final String PLAYER_STATS = "playerStats";

    public static final String GOAL = "goal";
    public static final String YELLOW_CARD = "yellowcard";
    public static final String RED_CARD = "redcard";
    public static final String YELLOW_RED = "yellowred";
    public static final String SUBSTITUTION = "substitution";
    public static final String KICK_OFF = "KICK-OFF";
    public static final String HALF_TIME = "HALF-TIME";
    public static final String FULL_TIME = "FULL-TIME";

    public static final String YELLOW = "yellow";
    public static final String RED = "red";
}