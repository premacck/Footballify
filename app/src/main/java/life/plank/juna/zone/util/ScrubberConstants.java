package life.plank.juna.zone.util;

/**
 * Created by plank-niraj on 06-02-2018.
 */

public class ScrubberConstants {

    static final int SCRUBBER_VIEW_PROGRESS = 2;
    static final int SCRUBBER_VIEW_HALF_TIME = 1;
    static final int SCRUBBER_VIEW_GOAL = 3;
    static final int SCRUBBER_VIEW_SUBSTITUTE = 5;
    static final int SCRUBBER_VIEW_CARDS = 6;
    static final int SCRUBBER_VIEW_CURSOR = 4;
    static final int SCRUBBER_PRE_MATCH = 60;
    static final int SCRUBBER_POST_MATCH = 60;
    static final int SCRUBBER_VIEW_TOTAL_WINDOW = 105;
    static final int SCRUBBER_VIEW_HALF_TIME_WINDOW = 5;
    private String scrubberHalf = "half";
    private String scrubberNormal = "normal";
    private String scrubberGoal = "goal";
    private String scrubberCursor = "cursor";
    private String scrubberSubstitute = "substitute";
    private String scrubberCard = "card";
    private String scrubberPost = "post";

    public static int getScrubberViewHalfTimeWindow() {
        return SCRUBBER_VIEW_HALF_TIME_WINDOW;
    }

    public static int getScrubberViewProgress() {
        return SCRUBBER_VIEW_PROGRESS;
    }

    public static int getScrubberViewHalfTime() {
        return SCRUBBER_VIEW_HALF_TIME;
    }

    public static int getScrubberViewGoal() {
        return SCRUBBER_VIEW_GOAL;
    }

    public static int getScrubberViewSubstitute() {
        return SCRUBBER_VIEW_SUBSTITUTE;
    }

    public static int getScrubberViewCards() {
        return SCRUBBER_VIEW_CARDS;
    }

    public static int getScrubberViewCursor() {
        return SCRUBBER_VIEW_CURSOR;
    }

    public static int getScrubberPreMatch() {
        return SCRUBBER_PRE_MATCH;
    }

    public static int getScrubberPostMatch() {
        return SCRUBBER_POST_MATCH;
    }

    public static int getScrubberViewTotalWindow() {
        return SCRUBBER_VIEW_TOTAL_WINDOW;
    }

    public String getScrubberHalf() {
        return scrubberHalf;
    }

    public String getScrubberNormal() {
        return scrubberNormal;
    }

    public String getScrubberGoal() {
        return scrubberGoal;
    }

    public String getScrubberCursor() {
        return scrubberCursor;
    }

    public String getScrubberSubstitute() {
        return scrubberSubstitute;
    }

    public String getScrubberCard() {
        return scrubberCard;
    }

    public String getScrubberPost() {
        return scrubberPost;
    }
}
