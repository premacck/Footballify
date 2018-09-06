package life.plank.juna.zone.util;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.model.ScoreFixture;

import static life.plank.juna.zone.util.DataUtil.formatInt;

public class DateUtil {

    private static final String ISO_DATE_STRING = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String HEADER_DATE_STRING = "EEE dd MMM yyyy";
    private static final String SCHEDULED_DATE_STRING = "EEE dd MM";
    private static final String FUTURE_DATE_FORM_STRING = "HH:mm";

    private static final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat(ISO_DATE_STRING, Locale.getDefault());
    private static final SimpleDateFormat HEADER_DATE_FORMAT = new SimpleDateFormat(HEADER_DATE_STRING, Locale.getDefault());

    private static Date getIsoFormattedDate(String dateString) throws ParseException {
        return ISO_DATE_FORMAT.parse(dateString);
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

    static long getTimeDiffFromNow(Date date) {
        return getTimeFromObject(date) - getTimeFromObject(new Date());
    }

    public static String getFormattedDate(Context context, ScoreFixture scoreFixture) {
        if (Objects.equals(scoreFixture.getTimeStatus(), context.getString(R.string.full_match_time))) {
            return "FT, " +
                    (wasMatchYesterday(scoreFixture.getMatchStartTime()) ?
                            context.getString(R.string.yesterday) :
                            new SimpleDateFormat(HEADER_DATE_STRING, Locale.getDefault()).format(scoreFixture.getMatchStartTime()));
        } else {
            switch (getDateDiffFromToday(scoreFixture.getMatchStartTime())) {
                case 0:
                    return context.getString(R.string.today);
                case 1:
                    return context.getString(R.string.tomorrow);
                default:
                    return new SimpleDateFormat(SCHEDULED_DATE_STRING, Locale.getDefault()).format(scoreFixture.getMatchStartTime());
            }
        }
    }

    public static boolean wasMatchYesterday(Date matchStartTime) {
        long timeDifferenceInHours = DateUtil.getDifferenceInHours((matchStartTime), new Date());
        return timeDifferenceInHours < -24 && timeDifferenceInHours > -48;
    }

    static String getFutureMatchTime(Date matchStartTime) {
        return new SimpleDateFormat(FUTURE_DATE_FORM_STRING, Locale.getDefault()).format(matchStartTime);
    }

    public static String getDateHeader(Date matchStartTime) {
        int dateDiff = getDateDiffFromToday(matchStartTime);
        switch (dateDiff) {
            case -1:
                return ZoneApplication.getContext().getString(R.string.yesterday);
            case 0:
                return ZoneApplication.getContext().getString(R.string.today);
            case 1:
                return ZoneApplication.getContext().getString(R.string.tomorrow);
            default:
                return HEADER_DATE_FORMAT.format(matchStartTime);
        }
    }
}