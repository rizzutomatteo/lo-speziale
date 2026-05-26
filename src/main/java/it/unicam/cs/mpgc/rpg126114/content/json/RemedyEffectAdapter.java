package it.unicam.cs.mpgc.rpg126114.content.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.effect.HumoralShiftEffect;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.effect.PurifyEffect;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.effect.RemedyEffect;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.effect.ToxicityEffect;
import it.unicam.cs.mpgc.rpg126114.model.humor.Humor;

import java.lang.reflect.Type;

/**
 * Teaches Gson how to read and write the {@link RemedyEffect} hierarchy.
 *
 * <p>Effects are polymorphic, so the JSON carries a {@code type} discriminator and
 * this adapter maps it to the right implementation. Registering it on the
 * {@code GsonBuilder} is what lets a remedy's effects be declared in data without
 * the loader knowing the concrete types in advance.
 */
public final class RemedyEffectAdapter
        implements JsonSerializer<RemedyEffect>, JsonDeserializer<RemedyEffect> {

    private static final String TYPE = "type";
    private static final String HUMOR = "humor";
    private static final String AMOUNT = "amount";

    @Override
    public RemedyEffect deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject object = json.getAsJsonObject();
        String type = requireString(object, TYPE);
        return switch (type) {
            case "humoral_shift" ->
                    new HumoralShiftEffect(humor(requireString(object, HUMOR)), requireInt(object, AMOUNT));
            case "toxicity" -> new ToxicityEffect(requireInt(object, AMOUNT));
            case "purify" -> new PurifyEffect(requireInt(object, AMOUNT));
            default -> throw new JsonParseException("unknown remedy effect type: " + type);
        };
    }

    @Override
    public JsonElement serialize(RemedyEffect src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        switch (src) {
            case HumoralShiftEffect shift -> {
                object.addProperty(TYPE, "humoral_shift");
                object.addProperty(HUMOR, shift.getHumor().name());
                object.addProperty(AMOUNT, shift.getAmount());
            }
            case ToxicityEffect toxicity -> {
                object.addProperty(TYPE, "toxicity");
                object.addProperty(AMOUNT, toxicity.getAmount());
            }
            case PurifyEffect purify -> {
                object.addProperty(TYPE, "purify");
                object.addProperty(AMOUNT, purify.getAmount());
            }
            default -> throw new JsonParseException("unsupported remedy effect: " + src.getClass());
        }
        return object;
    }

    private static Humor humor(String name) {
        try {
            return Humor.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new JsonParseException("unknown humor: " + name, e);
        }
    }

    private static String requireString(JsonObject object, String field) {
        if (!object.has(field)) {
            throw new JsonParseException("missing field '" + field + "'");
        }
        return object.get(field).getAsString();
    }

    private static int requireInt(JsonObject object, String field) {
        if (!object.has(field)) {
            throw new JsonParseException("missing field '" + field + "'");
        }
        return object.get(field).getAsInt();
    }
}
