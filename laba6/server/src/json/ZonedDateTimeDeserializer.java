package json;

import com.google.gson.*;
import common.exceptions.InvalidDateFormatException;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static common.utils.DateConverter.parseZonedDateTime;

/**
 * type adapter for json deserialization
 */
public class ZonedDateTimeDeserializer implements JsonDeserializer<ZonedDateTime> {
    @Override
    public ZonedDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        try{
            return parseZonedDateTime(json.getAsJsonPrimitive().getAsString());
        }
        catch (InvalidDateFormatException e){
            throw new JsonParseException("");
        }
    }
}