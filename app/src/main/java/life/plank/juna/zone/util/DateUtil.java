package life.plank.juna.zone.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static final DateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public static Date getIsoFormattedDate(String dateString) throws ParseException {
        return ISO_DATE_FORMAT.parse( dateString );
    }

    public static long getDifferenceInHours(Date firstDate, Date secondDate) {
        long millisDifference = firstDate.getTime() - secondDate.getTime();
        return millisDifference/(3600000L);
    }
}
