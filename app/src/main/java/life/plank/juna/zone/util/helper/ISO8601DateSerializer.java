package life.plank.juna.zone.util.helper;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static life.plank.juna.zone.util.DateUtil.ISO_DATE_FORMAT;

public class ISO8601DateSerializer implements JsonDeserializer<Date>, JsonSerializer<Date> {

    private SimpleDateFormat format;

    public ISO8601DateSerializer() {
        format = ISO_DATE_FORMAT;
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @Override
    public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
        String date = element.getAsString();
        try {
            return format.parse(date);
        }
        catch (ParseException exp) {
            return null;
        }
    }

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return src == null ? null : new JsonPrimitive(format.format(src));
    }
}
