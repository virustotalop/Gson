package net.pixelverse.gson.internal;

import com.google.gson.*;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SkippingJsonAdapter extends TypeAdapter<JsonElement> {
    private List<String> toSkip;

    public SkippingJsonAdapter(String...skippedNames) {
        toSkip = Arrays.asList(skippedNames);
    }

    @Override public JsonElement read(JsonReader in) throws IOException {
        switch (in.peek()) {
            case STRING:
                return new JsonPrimitive(in.nextString());
            case NUMBER:
                String number = in.nextString();
                return new JsonPrimitive(new LazilyParsedNumber(number));
            case BOOLEAN:
                return new JsonPrimitive(in.nextBoolean());
            case NULL:
                in.nextNull();
                return JsonNull.INSTANCE;
            case BEGIN_ARRAY:
                JsonArray array = new JsonArray();
                in.beginArray();
                while (in.hasNext()) {
                    array.add(read(in));
                }
                in.endArray();
                return array;
            case BEGIN_OBJECT:
                JsonObject object = new JsonObject();
                in.beginObject();
                while (in.hasNext()) {
                    String name = in.nextName();
                    if (toSkip.contains(name)) {
                        in.skipValue();
                    } else {
                        object.add(name, read(in));
                    }
                }
                in.endObject();
                return object;
            case END_DOCUMENT:
            case NAME:
            case END_OBJECT:
            case END_ARRAY:
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override public void write(JsonWriter out, JsonElement value) throws IOException {
        if (value == null || value.isJsonNull()) {
            out.nullValue();
        } else if (value.isJsonPrimitive()) {
            JsonPrimitive primitive = value.getAsJsonPrimitive();
            if (primitive.isNumber()) {
                out.value(primitive.getAsNumber());
            } else if (primitive.isBoolean()) {
                out.value(primitive.getAsBoolean());
            } else {
                out.value(primitive.getAsString());
            }

        } else if (value.isJsonArray()) {
            out.beginArray();
            for (JsonElement e : value.getAsJsonArray()) {
                write(out, e);
            }
            out.endArray();

        } else if (value.isJsonObject()) {
            out.beginObject();
            for (Map.Entry<String, JsonElement> e : value.getAsJsonObject().entrySet()) {
                out.name(e.getKey());
                write(out, e.getValue());
            }
            out.endObject();

        } else {
            throw new IllegalArgumentException("Couldn't write " + value.getClass());
        }
    }
}
