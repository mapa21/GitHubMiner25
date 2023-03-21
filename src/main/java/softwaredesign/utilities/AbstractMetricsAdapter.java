package softwaredesign.utilities;

import com.fasterxml.jackson.databind.JsonSerializable;
import com.google.gson.*;
import softwaredesign.extraction.Metric;
import softwaredesign.extraction.SingleData;
import softwaredesign.extraction.metrics.CommitsPerDay;

import java.lang.reflect.Type;
import java.util.List;

/* This class exists to handle JSON serialization and deserialization of abstract Metrics elements */
public class AbstractMetricsAdapter implements JsonSerializer<Metric>, JsonDeserializer<Metric> {

    @Override
    public JsonElement serialize(Metric src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
        result.add("properties", context.serialize(src, src.getClass()));
        return result;
    }

    @Override
    public Metric deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");

        try {
            return context.deserialize(element, Class.forName("softwaredesign.extraction.metrics." + type));
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException("Unknown element type: " + type, cnfe);
        }
    }
}
