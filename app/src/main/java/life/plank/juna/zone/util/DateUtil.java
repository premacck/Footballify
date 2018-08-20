package life.plank.juna.zone.util;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ScoreFixtureModel;
import life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection;

import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.wasMatchYesterday;
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

    private static int getDateDiffFromToday(Date date) {
        return getDateFromObject(date) - getDateFromObject(new Date());
    }

    static long getTimeDiffFromNow(Date date) {
        return getTimeFromObject(date) - getTimeFromObject(new Date());
    }

    public static String getFormattedDate(Context context, ScoreFixtureModel scoreFixture) {
        if (Objects.equals(scoreFixture.getTimeStatus(), context.getString(R.string.full_match_time))) {
            return "FT, " +
                    (wasMatchYesterday(scoreFixture.getMatchStartTime()) ?
                            context.getString(R.string.yesterday) :
                            new SimpleDateFormat(HEADER_DATE_STRING, Locale.getDefault()).format(scoreFixture.getMatchStartTime()));
        } else {
            int dateDiff = getDateDiffFromToday(scoreFixture.getMatchStartTime());
            if (dateDiff == 0)
                return context.getString(R.string.today);
            else if (dateDiff == 1)
                return context.getString(R.string.tomorrow);
            else
                return new SimpleDateFormat(SCHEDULED_DATE_STRING, Locale.getDefault()).format(scoreFixture.getMatchStartTime());
        }
    }

    static String getFutureMatchTime(Date matchStartTime) {
        return new SimpleDateFormat(FUTURE_DATE_FORM_STRING, Locale.getDefault()).format(matchStartTime);
    }

    public static String getDateHeader(Context context, FixtureSection section, Date matchStartTime) {
        return getCurrentSectionHeader(context, section, matchStartTime);
    }

    private static String getCurrentSectionHeader(Context context, FixtureSection section, Date matchStartTime) {
        switch (section) {
            case LIVE_MATCHES:
                return context.getString(R.string.today);
            case TOMORROWS_MATCHES:
                return context.getString(R.string.tomorrow);
            default:
                return HEADER_DATE_FORMAT.format(matchStartTime);
        }
    }
}