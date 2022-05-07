package json;

import com.google.gson.*;
import common.exceptions.InvalidDateFormatException;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static common.utils.DateConverter.parseLocalDateTime;

/**
 * type adapter for json deserialization
 */
public class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        try{
            return parseLocalDateTime(json.getAsJsonPrimitive().getAsString());
        }
        catch (InvalidDateFormatException e){
            throw new JsonParseException("");
        }
    }


}