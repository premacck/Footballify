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

    public static final String STANDINGS = "Standings";
    public static final String TEAM_STATS = "Team Stats";
    public static final String PLAYER_STATS = "Player Stats";

    public static final String GOAL = "goal";
    public static final String YELLOW_CARD = "yellowcard";
    public static final String RED_CARD = "redcard";
    public static final String YELLOW_RED = "yellowred";
    public static final String SUBSTITUTION = "substitution";
    public static final String KICK_OFF = "KICK-OFF";
    public static final String HALF_TIME = "HALF-TIME";
    public static final String FULL_TIME = "FULL-TIME";
    public static final String HALF_TIME_LOWERCASE = "Half-time";
    public static final String FULL_TIME_LOWERCASE = "Full-time";

    //    TODO : this field is for demo purposes only. Remove this constant after getting real scrubber data from backend
    public static final String FOUL = "foul";

    public static final String YELLOW = "yellow";
    public static final String RED = "red";
    public static final String LIVE = "LIVE";
    public static final String LIVE_TIME = "00.00";
    public static final String HT = "HT";
    public static final String FT = "FT";

    public static final String MATCH_EVENTS = "matchEventList";
    public static final String SCORE_DATA = "scoreData";
    public static final String TIME_STATUS = "timeStatus";
    public static final String COMMENTARY_DATA = "commentaryList";
    public static final String LINEUPS_DATA = "liveTeamFormations";

    public static final String FIRST_HALF_ENDED_ = "First Half ended - ";
    public static final String SECOND_HALF_ENDED_ = "Second Half ended - ";
    public static final String THATS_ALL = "Thats all ";
    public static final String GOAL_ = "Goal! ";
    public static final String CORNER_ = "Corner - ";
    public static final String SUBSTITUTION_ = "Substitution - ";
    public static final String OFFSIDE_ = "Offside - ";
    public static final String YELLOW_CARD_ = "yellow card";
    public static final String RED_CARD_ = "red card";
    public static final String FREE_KICK_ = "free kick";

    public static final String DASH = " - ";
    public static final String WIDE_DASH = "  -  ";
    public static final String WIDE_SPACE = "     ";
    public static final String SPACE = "   ";

    public static final String VIDEO = "Video";
    public static final String AUDIO = "Audio";
    public static final String IMAGE = "Image";
    public static final String CAMERA = "Camera";
    public static final String GALLERY = "Gallery";
    public static final String TYPE_VIDEO = "video/";
    public static final String TYPE_IMAGE = "image/";
    public static final String PNG = "png";
    public static final String JPEG = "jpeg";
    public static final String BMP = "bmp";
    public static final String GIF = "gif";
    
    public static final String PAST_MATCHES = "PAST_MATCHES";
    public static final String TODAY_MATCHES = "TODAY_MATCHES";
    public static final String SCHEDULED_MATCHES = "SCHEDULED_MATCHES";
}