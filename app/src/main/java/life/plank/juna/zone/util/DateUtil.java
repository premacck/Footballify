package life.plank.juna.zone.util;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ScoreFixtureModel;

import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.wasMatchYesterday;
import static life.plank.juna.zone.util.DataUtil.formatInt;

public class DateUtil {

    private static final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    private static final SimpleDateFormat HEADER_DATE_FORMAT = new SimpleDateFormat("EEE dd MMM yyyy", Locale.getDefault());

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
        dateString = "" + cal.get(Calendar.YEAR) + formatInt(monthInt) + formatInt(dateInt);
        return Integer.parseInt(dateString);
    }

    private static int getTimeFromObject(Date date) {
        String dateString;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int monthInt = cal.get(Calendar.MONTH);
        int dateInt = cal.get(Calendar.DATE);
        int hourInt = cal.get(Calendar.HOUR_OF_DAY);
        int minuteInt = cal.get(Calendar.MINUTE);
        int secondInt = cal.get(Calendar.SECOND);
        dateString = "" + cal.get(Calendar.YEAR) + formatInt(monthInt) + formatInt(dateInt) +
                formatInt(hourInt) + formatInt(minuteInt) + formatInt(secondInt);
        return Integer.parseInt(dateString);
    }

    private static int getDateDiffFromToday(Date date) {
        return getDateFromObject(date) - getDateFromObject(new Date());
    }

    static int getTimeDiffFromNow(Date date) {
        return getTimeFromObject(date) - getTimeFromObject(new Date());
    }

    public static String getFormattedDate(Context context, ScoreFixtureModel scoreFixture) {
        if (Objects.equals(scoreFixture.getTimeStatus(), "FT")) {
            return "FT, " +
                    (wasMatchYesterday(scoreFixture.getMatchStartTime()) ?
                            context.getString(R.string.yesterday) :
                            new SimpleDateFormat("EEE dd MMM yyyy", Locale.getDefault()).format(scoreFixture.getMatchStartTime()));
        } else {
            int dateDiff = getDateDiffFromToday(scoreFixture.getMatchStartTime());
            if (dateDiff == 0)
                return context.getString(R.string.today);
            else if (dateDiff == 1)
                return context.getString(R.string.tomorrow);
            else
                return new SimpleDateFormat("EEE dd MM", Locale.getDefault()).format(scoreFixture.getMatchStartTime());
        }
    }

    static String getFutureMatchTime(Date matchStartTime) {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(matchStartTime);
    }

    public static String getDateHeader(Date matchStartTime) {
        return HEADER_DATE_FORMAT.format(matchStartTime);
    }

    public static Date getDateFromUTCTimestamp(long mTimestamp) {
        Date date;
        try {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            cal.setTimeInMillis(mTimestamp);
            String dateString = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss", cal.getTimeInMillis()).toString();

            SimpleDateFormat formatter = ISO_DATE_FORMAT;
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(dateString);

            SimpleDateFormat dateFormatter = ISO_DATE_FORMAT;
            dateFormatter.setTimeZone(TimeZone.getDefault());
            date = getIsoFormattedDate(dateFormatter.format(value));
            return date;
        } catch (Exception e) {
            Log.e("DATE", e.getMessage());
            return null;
        }
    }
}