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

public class DateUtil {

    private static final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

    private static Date getIsoFormattedDate(String dateString) throws ParseException {
        return ISO_DATE_FORMAT.parse(dateString);
    }

    public static long getDifferenceInHours(Date firstDate, Date secondDate) {
        long millisDifference = firstDate.getTime() - secondDate.getTime();
        Log.e("Date", "get_Time" + millisDifference);
        return millisDifference / (3600000L);
    }

    public static int getDateFromObject(Date date) {
        String dateString;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int monthInt = cal.get(Calendar.MONTH);
        int dateInt = cal.get(Calendar.DATE);
        dateString = "" + cal.get(Calendar.YEAR) +
                (monthInt > 9 ? monthInt : "0" + monthInt) +
                (dateInt > 9 ? dateInt : "0" + dateInt);
        return Integer.parseInt(dateString);
    }

    public static String getFormattedDate(Context context, ScoreFixtureModel scoreFixture) {
        if (Objects.equals(scoreFixture.getTimeStatus(), "FT")) {
            return "FT, " +
                    (wasMatchYesterday(scoreFixture.getMatchStartTime()) ?
                            context.getString(R.string.yesterday) :
                            new SimpleDateFormat("EEE dd/MM", Locale.getDefault()).format(scoreFixture.getMatchStartTime()));
        } else {
            return new SimpleDateFormat("EEE dd/MM, hh.mm aa", Locale.getDefault()).format(scoreFixture.getMatchStartTime());
        }
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
            e.printStackTrace();
            return null;
        }
    }
}