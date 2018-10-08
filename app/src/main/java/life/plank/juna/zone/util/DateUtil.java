package life.plank.juna.zone.util;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.MatchFixture;
import life.plank.juna.zone.util.AppConstants.MatchTimeVal;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static life.plank.juna.zone.util.AppConstants.FOUR_HOURS_MILLIS;
import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_ABOUT_TO_START;
import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_ABOUT_TO_START_BOARD_ACTIVE;
import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_COMPLETED_TODAY;
import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_LIVE;
import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_PAST;
import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_SCHEDULED_LATER;
import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_SCHEDULED_TODAY;
import static life.plank.juna.zone.util.AppConstants.ONE_DAY_MILLIS;
import static life.plank.juna.zone.util.AppConstants.ONE_HOUR_MILLIS;
import static life.plank.juna.zone.util.AppConstants.TWO_HOURS_MILLIS;
import static life.plank.juna.zone.util.DataUtil.formatInt;

public class DateUtil {

    private static final String TIMELINE_HEADER_DATE_STRING = "EEE, dd MMM";
    private static final String ISO_DATE_STRING = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String HEADER_DATE_STRING = "EEE dd MMM yyyy";
    private static final String SCHEDULED_DATE_STRING = "EEE dd MM";
    private static final String FUTURE_DATE_FORM_STRING = "HH:mm";
    private static final String MINUTE_SECOND_TIME_STRING = "mm:ss";
    private static final String HOUR_MINUTE_SECOND_TIME_STRING = "HH:mm:ss";
    private static final String SCHEDULED_MATCH_DATE_STRING = "EEE dd/MM\nK.mm aaa";
    public static final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat(ISO_DATE_STRING, Locale.getDefault());
    private static final SimpleDateFormat HEADER_DATE_FORMAT = new SimpleDateFormat(HEADER_DATE_STRING, Locale.getDefault());
    private static final SimpleDateFormat TIMELINE_HEADER_DATE_FORMAT = new SimpleDateFormat(TIMELINE_HEADER_DATE_STRING, Locale.getDefault());
    private static final SimpleDateFormat FUTURE_DATE_FORMAT = new SimpleDateFormat(FUTURE_DATE_FORM_STRING, Locale.getDefault());
    private static final SimpleDateFormat MINUTE_SECOND_TIME_FORMAT = new SimpleDateFormat(MINUTE_SECOND_TIME_STRING, Locale.getDefault());
    private static final SimpleDateFormat SCHEDULED_MATCH_DATE_FORMAT = new SimpleDateFormat(SCHEDULED_MATCH_DATE_STRING, Locale.getDefault());
    public static final SimpleDateFormat HOUR_MINUTE_SECOND_DATE_FORMAT = new SimpleDateFormat(HOUR_MINUTE_SECOND_TIME_STRING, Locale.getDefault());

    private static Date getIsoFormattedDate(String dateString) throws ParseException {
        return ISO_DATE_FORMAT.parse(dateString);
    }

    public static String getRequestDateStringOfNow() {
        ISO_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        return ISO_DATE_FORMAT.format(Calendar.getInstance().getTime());
    }

    private static String getIsoFormattedDate(Date date) {
        return ISO_DATE_FORMAT.format(date);
    }

    public static long getDifferenceInHours(Date firstDate, Date secondDate) {
        long millisDifference = firstDate.getTime() - secondDate.getTime();
        return millisDifference / (3600000L);
    }

    public static int getDateFromObject(Date date) {
        String dateString;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int monthInt = cal.get(Calendar.MONTH);
        int dateInt = cal.get(Calendar.DATE);
        dateString = cal.get(Calendar.YEAR) + formatInt(monthInt) + formatInt(dateInt);
        return Integer.parseInt(dateString);
    }

    private static long getTimeFromObject(Date date) {
        String dateString;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int monthInt = cal.get(Calendar.MONTH);
        int dateInt = cal.get(Calendar.DATE);
        int hourInt = cal.get(Calendar.HOUR_OF_DAY);
        int minuteInt = cal.get(Calendar.MINUTE);
        dateString = cal.get(Calendar.YEAR) + formatInt(monthInt) + formatInt(dateInt) +
                formatInt(hourInt) + formatInt(minuteInt);
        return Long.parseLong(dateString);
    }

    public static int getDateDiffFromToday(Date date) {
        return getDateFromObject(date) - getDateFromObject(new Date());
    }

    public static long getTimeDiffFromNow(Date date) {
        return getTimeFromObject(date) - getTimeFromObject(new Date());
    }

    public static long getAbsoluteTimeDiffFromNow(Date date) {
        return Math.abs(new Date().getTime() - date.getTime());
    }

    public static String getFormattedDate(Context context, MatchFixture MatchFixture) {
        if (Objects.equals(MatchFixture.getTimeStatus(), context.getString(R.string.full_match_time))) {
            return "FT, " +
                    (wasMatchYesterday(MatchFixture.getMatchStartTime()) ?
                            context.getString(R.string.yesterday) :
                            new SimpleDateFormat(HEADER_DATE_STRING, Locale.getDefault()).format(MatchFixture.getMatchStartTime()));
        } else {
            switch (getDateDiffFromToday(MatchFixture.getMatchStartTime())) {
                case 0:
                    return context.getString(R.string.today);
                case 1:
                    return context.getString(R.string.tomorrow);
                default:
                    return new SimpleDateFormat(SCHEDULED_DATE_STRING, Locale.getDefault()).format(MatchFixture.getMatchStartTime());
            }
        }
    }

    public static boolean wasMatchYesterday(Date matchStartTime) {
        long timeDifferenceInHours = DateUtil.getDifferenceInHours((matchStartTime), new Date());
        return timeDifferenceInHours < -24 && timeDifferenceInHours > -48;
    }

    static String getFutureMatchTime(Date matchStartTime) {
        return FUTURE_DATE_FORMAT.format(matchStartTime);
    }

    public static CharSequence getDateHeader(Date matchStartTime) {
        int dateDiff = getDateDiffFromToday(matchStartTime);
        switch (dateDiff) {
            case -1:
                return ZoneApplication.getContext().getString(R.string.yesterday);
            case 0:
                SpannableStringBuilder builder = new SpannableStringBuilder(ZoneApplication.getContext().getString(R.string.today));
                builder.setSpan(
                        new ForegroundColorSpan(ResourcesCompat.getColor(ZoneApplication.getContext().getResources(), R.color.fab_button_pink, null)),
                        0, 5, SPAN_EXCLUSIVE_EXCLUSIVE
                );
                return builder;
            case 1:
                return ZoneApplication.getContext().getString(R.string.tomorrow);
            default:
                return HEADER_DATE_FORMAT.format(matchStartTime);
        }
    }

    public static String getTimelineDateHeader(Date matchStartTime) {
        int dateDiff = getDateDiffFromToday(matchStartTime);
        switch (dateDiff) {
            case -1:
                return ZoneApplication.getContext().getString(R.string.yesterday);
            case 0:
                return ZoneApplication.getContext().getString(R.string.today);
            case 1:
                return ZoneApplication.getContext().getString(R.string.tomorrow);
            default:
                return TIMELINE_HEADER_DATE_FORMAT.format(matchStartTime);
        }
    }

    static String getDateForScrubber(long milliSeconds) {
        try {
            return FUTURE_DATE_FORMAT.format(new Date(milliSeconds));
        } catch (Exception e) {
            return String.valueOf(milliSeconds);
        }
    }

    public static String getHourMinuteSecondFormatDate(Date date) {
        return HOUR_MINUTE_SECOND_DATE_FORMAT.format(date);
    }

    public static String getMinuteSecondFormatDate(Date date) {
        return MINUTE_SECOND_TIME_FORMAT.format(date);
    }

    public static String getScheduledMatchDateString(Date date) {
        return SCHEDULED_MATCH_DATE_FORMAT.format(date);
    }

    public static String getTimeInFootballFormat(Date date) {
        String displayTime = getHourMinuteSecondFormatDate(date);
        String[] splitDisplayTime = displayTime.split(":");
        if (Integer.valueOf(splitDisplayTime[0]) <= 0) {
            return splitDisplayTime[1] + ":" + splitDisplayTime[2];
        } else {
            return (Integer.valueOf(splitDisplayTime[0]) * 60) + Integer.valueOf(splitDisplayTime[1]) + ":" + splitDisplayTime[2];
        }
    }

    /**
     * Method for determining the value the match time is currently in<br/>
     * It can be one of thw values in {@link MatchTimeVal} annotation, which are:
     * <br/>-> MATCH_PAST:
     * <br/>-> MATCH_COMPLETED_TODAY:
     * <br/>-> MATCH_LIVE:
     * <br/>-> MATCH_ABOUT_TO_START:
     * <br/>-> MATCH_ABOUT_TO_START_BOARD_ACTIVE:
     * <br/>-> MATCH_SCHEDULED_TODAY:
     * <br/>-> MATCH_SCHEDULED_LATER:
     * @param matchStartTime the match's start time
     * @param isForBoard It should be true when calling this method to get time values of a match
     *                   to update the board info components, false otherwise.
     * @return an int value, which is one of {@link MatchTimeVal} annotation
     */
    @MatchTimeVal
    public static int getMatchTimeValue(Date matchStartTime, boolean isForBoard) {
        int dateDiff = getDateDiffFromToday(matchStartTime);
        if (dateDiff < 0) {
//                past
            return MATCH_PAST;
        } else {
            long timeDiff = matchStartTime.getTime() - new Date().getTime();
//            Checking if 2 hours have elapsed since match start time
            if (timeDiff < 0) {
                if (timeDiff > -TWO_HOURS_MILLIS) {
//                    live
                    return MATCH_LIVE;
                } else {
//                    Completed sometime today
                    return MATCH_COMPLETED_TODAY;
                }
            }
            else if (timeDiff <= ONE_DAY_MILLIS) {
                if (timeDiff <= ONE_HOUR_MILLIS && isForBoard) {
//                    match is about to start in one hour
                    return MATCH_ABOUT_TO_START;
                }
                if (timeDiff <= FOUR_HOURS_MILLIS && isForBoard) {
//                    match is about to start in four hours, and board is active
                    return MATCH_ABOUT_TO_START_BOARD_ACTIVE;
                }
//                scheduled today (the match is in the next 24 hours)
                return MATCH_SCHEDULED_TODAY;
            } else {
//                scheduled later
                return MATCH_SCHEDULED_LATER;
            }
        }
    }
}