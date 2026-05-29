package it.unicam.cs.mpgc.rpg126114.persistence;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Tells Gson how to read and write a {@link LocalDate}, which it does not handle
 * out of the box. The date is stored in ISO form, e.g. {@code 2026-06-13}.
 */
public final class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        try {
            return LocalDate.parse(json.getAsString());
        } catch (DateTimeParseException e) {
            throw new JsonParseException("invalid date: " + json, e);
        }
    }
}
