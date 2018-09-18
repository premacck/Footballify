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
import java.util.concurrent.TimeUnit;

import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.model.MatchFixture;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
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
        long diffInMillis = new Date().getTime() - date.getTime();
        return (int) TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public static long getTimeDiffFromNow(Date date) {
        return Math.abs(new Date().getTime() - date.getTime());
    }

    public static String getFormattedDate(Context context, MatchFixture matchFixture) {
        if (Objects.equals(matchFixture.getTimeStatus(), context.getString(R.string.full_match_time))) {
            return "FT, " +
                    (wasMatchYesterday(matchFixture.getMatchStartTime()) ?
                            context.getString(R.string.yesterday) :
                            new SimpleDateFormat(HEADER_DATE_STRING, Locale.getDefault()).format(matchFixture.getMatchStartTime()));
        } else {
            switch (getDateDiffFromToday(matchFixture.getMatchStartTime())) {
                case 0:
                    return context.getString(R.string.today);
                case 1:
                    return context.getString(R.string.tomorrow);
                default:
                    return new SimpleDateFormat(SCHEDULED_DATE_STRING, Locale.getDefault()).format(matchFixture.getMatchStartTime());
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

    public static String getMinuteSecondFormatDate(Date date) {
        return HOUR_MINUTE_SECOND_DATE_FORMAT.format(date);
    }

    public static String getMinutesElapsedFrom(Date date) {
        return MINUTE_SECOND_TIME_FORMAT.format(new Date(getTimeDiffFromNow(date)));
    }

    public static String getScheduledMatchDateString(Date date) {
        return SCHEDULED_MATCH_DATE_FORMAT.format(date);
    }
}